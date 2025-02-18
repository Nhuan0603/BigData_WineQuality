import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;

public class KMapper extends Mapper<LongWritable, Text, LongWritable, PointWritable> {

	private PointWritable[] currCentroids;
	private final LongWritable centroidId = new LongWritable();
	private final PointWritable pointInput = new PointWritable();

	@Override
	public void setup(Context context) {
		int nClusters = Integer.parseInt(context.getConfiguration().get("k"));

		this.currCentroids = new PointWritable[nClusters];
		for (int i = 0; i < nClusters; i++) {
			String[] centroid = context.getConfiguration().getStrings("C" + i);
			// this.currCentroids[i] = new PointWritable(centroid[0].split(","));
			this.currCentroids[i] = new PointWritable(centroid);
		}
	}

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		

		if (key.get() == 0 && value.toString().contains("fixed acidity")) {
			return;
		}
		

		String[] arrPropPoint = value.toString().split(",");
		// Loại bỏ cột cuối cùng
		String[] pointData = new String[arrPropPoint.length - 1];
	    System.arraycopy(arrPropPoint, 0, pointData, 0, arrPropPoint.length - 1);
		
		
//		pointInput.set(arrPropPoint);
	    // Gán dữ liệu vào PointWritable
	    pointInput.set(pointData);
		
		double minDistance = Double.MAX_VALUE;
		int centroidIdNearest = 0;
		for (int i = 0; i < currCentroids.length; i++) {
			System.out.println("currCentroids[" + i + "]=" + currCentroids[i].toString());
			double distance = pointInput.calcDistance(currCentroids[i]);
			if (distance < minDistance) {
				centroidIdNearest = i;
				minDistance = distance;
			}
		}
		centroidId.set(centroidIdNearest);
		context.write(centroidId, pointInput);
	}
}