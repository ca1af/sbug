# Redis

## 1️⃣ Redis로 JWT의 보안 취약점 보완
JWT는 세션 방식보다 DB의 부담이 적고, 빠르다는 장점을 가집니다.<br>
그러나 사용자에게 Token을 직접 발급하기 때문에 탈취나 위조의 가능성이 있습니다.<br>
우리는 이미 리프레쉬 토큰 사용해어느정도보안 향상이 가능하나 이것만으로는 충분하지 않다고 생각했습니다.<br> 

![image](https://user-images.githubusercontent.com/117061584/223668618-20b8a98d-62b0-488a-b58c-8a432c41d334.png)

### 액세스 토큰은 클라이언트가 가지고 있지만 리프레쉬 토큰은 레디스 캐시에 저장함으로써<br> 토큰이 탈취 당하는 등 토큰의 만료가 필요할 때 운영자가 조정할 수 있도록 Redis를 사용했습니다.


## 2️⃣ Redis를 이용한 캐싱
![image](https://user-images.githubusercontent.com/117061584/223668064-3fb07d1f-8993-4c67-82ad-181a88cfb450.png)  

Spring security 필터를 돌면서 계속해서 나가던 쿼리(Loadbyusername)를 레디스로 성능 개선을 진행했습니다.
