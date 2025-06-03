<h1>TutorTutee</h1>
화상과외 프로그램

## 접속 URL
- URL : https://tutor-tutee.shop/app/

## 📦 Repository

- 🔙 **BackEnd**: [Tutor-Tutee-Backend](https://github.com/moonjinho99/Tutor-Tutee-Backend)
- 🎨 **FrontEnd**: [TutorTutee-FrontEnd](https://github.com/ParkYongHo1/TutorTutee-FrontEnd)

---

## 👥 팀원 구성

<details>
  <summary>🔽 클릭하여 팀원 보기</summary>

  <br>

  ### 🛠 BackEnd
  | 이름 | 역할 | GitHub |
  |------|------|--------|
  | <img src="https://github.com/moonjinho99.png?size=50" width="50"> **문진호** | 백엔드 | [@moonjinho99](https://github.com/moonjinho99) |
  | <img src="https://github.com/qldmq.png?size=50" width="50"> **김서현** | 백엔드 | [@qldmq](https://github.com/qldmq) |

  ### 🎨 FrontEnd
  | 이름 | 역할 | GitHub |
  |------|------|--------|
  | <img src="https://github.com/ParkYongHo1.png?size=50" width="50"> **박용호** | 프론트엔드 | [@ParkYongHo1](https://github.com/ParkYongHo1) |

</details>

---

## 📺 개발환경
- <img src="https://img.shields.io/badge/IDE-%23121011?style=for-the-badge"> <img src="https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=Eclipse&logoColor=white"/>
- <img src="https://img.shields.io/badge/Tool-%23121011?style=for-the-badge"> <img src="https://img.shields.io/badge/MobaXterm-3A4655?style=for-the-badge&logo=MobaXterm&logoColor=white"/> ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
- <img src="https://img.shields.io/badge/Tech Stack-%23121011?style=for-the-badge"> <img src="https://img.shields.io/badge/java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/17-515151?style=for-the-badge"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
 <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=Amazon-AWS&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> 
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) 
- <img src="https://img.shields.io/badge/Database-%23121011?style=for-the-badge">![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
<br/><br/>
<hr>

## 🚩 개요
- 본 서비스는 SNS 기능과 실시간 화상 기술을 접목한 라이브 과외 플랫폼으로, 팔로우 기반의 네트워크를 활용하여 신뢰도 높은 1:N 화상 강의를 제공한다.
- 호스트(강사)는 자신을 팔로우 하는 수강자 중 일부를 선택하여 실시간 라이브 강의를 진행할 수 있으며, 수강자는 실시간 채팅을 통해 즉각적인 질문 및 소통이 가능하다.
 - 소셜 로그인을 통해 사용자 접근성을 강화하였고, 실시간 알림 기능으로 수업 참여율을 향상시킬 수 있다.


## 📝 담당 역할

- **데이터베이스 설계**  
  - 과외 서비스 전반을 고려한 테이블 구조 및 관계 설계

- **배포 환경 구축**  
  - AWS EC2, S3, RDS, Route 53을 활용한 백엔드/프론트엔드 배포

- **실시간 기능 구현**  
  - SSE: 실시간 알림 시스템  
  - WebSocket(STOMP): 실시간 채팅 기능  
  - WebRTC: 화상 과외를 위한 방, 사용자 세션처리 / 시그널링 서버 구축

- **성능 최적화 및 데이터 처리**  
  - Redis 기반 실시간 데이터 처리  
  - 서버 사이드 목록 조회 최적화

- **인증 및 보안**  
  - JWT 기반 인증 및 권한 관리  
  - OAuth 2.0 기반 네이버 소셜 로그인

- **핵심 기능 개발**  
  - 팔로우, 알림, 라이브방, 회원 관리 등 주요 기능 CRUD 구현


## 🔍 아키텍처 구조

<details>
  <summary>📌 클릭하여 아키텍처 구조 보기</summary>

  <br>

  ![아키텍처 구조 이미지](https://github.com/user-attachments/assets/2f8c6ffe-b40f-42b1-9de2-716b7d2386f3)


</details>



## 🔍 ERD

<details>
  <summary>📌 클릭하여 ERD 이미지 보기</summary>

  <br>

  ![ERD 이미지](https://github.com/user-attachments/assets/e59a4faf-c5b8-4807-8b57-523de13217b6)

</details>

## 🎬 시연

### 1. 소셜 로그인

<details>
  <summary>✅ 소셜 로그인 시연 보기</summary>

  <br>
  
![Image](https://github.com/user-attachments/assets/be331fcd-969a-41a0-bc0c-43a01f5fff64)

</details>

---

### 2. 실시간 알림

<details>
  <summary>🔔 실시간 알림 시연 보기</summary>

  <br>

  - 팔로우/팔로잉 알림  
  - 강의 시작 알림  
  - 게시글 작성 및 좋아요/싫어요 알림  

![Image](https://github.com/user-attachments/assets/3ded3439-b356-426c-8d43-5add1c17237c)

</details>

---

### 3. 화상 과외

<details>
  <summary>🎥 화상 과외 시연 보기</summary>

  <br>

  - 실시간 화면 공유  
  - 채팅 기능  

[호스트 - 참여자 초대, 화면공유]
<br>

![Image](https://github.com/user-attachments/assets/9a4d8e15-4627-42e2-9dc0-974a62feedc3)

[참여자 - 강의 참여, 채팅]
<br>

![Image](https://github.com/user-attachments/assets/e11e8031-e49e-41da-8293-5ec214d2f675)

[호스트, 참여자 - 호스트의 실시간 화면 공유]
<br>

![Image](https://github.com/user-attachments/assets/0a9ff831-e20f-4ee5-ab31-026e3495c08d)
</details>


