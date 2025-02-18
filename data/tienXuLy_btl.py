import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt

file_path = "D:/bigData/wineQuality.csv"  
data = pd.read_csv(file_path)



scaler = StandardScaler()
X_scaled = scaler.fit_transform(data)

processed_data = pd.DataFrame(X_scaled, columns=data.columns)
processed_data.to_csv("processed_wine_data.csv", index=False)  

distortions = []
K = range(1, 10)
for k in K:
    kmeans = KMeans(n_clusters=k, n_init=10, random_state=42)  
    kmeans.fit(X_scaled)
    distortions.append(kmeans.inertia_)


plt.figure(figsize=(8, 5))
plt.plot(K, distortions, 'bx-')
plt.xlabel('Number of clusters (k)')
plt.ylabel('Distortion')
plt.title('Elbow Method for Optimal k')
plt.show()

print("Dữ liệu đã xử lý được lưu thành công vào file 'processed_wine_data.csv'.")