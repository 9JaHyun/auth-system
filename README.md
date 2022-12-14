# auth-system
2022 원터데브캠프 1인 프로젝트 인증 시스템

# 진행 상황
## User-Service
- [X] 도메인 구현
- [X] 회원 가입 로직 구현
- [X] 이메일 전송 기능 구현
- [ ] 비밀번호 암호화 구현
- [ ] 비밀번호 찾기 구현

## Auth-Server
- [X] JWT 발급 (access, refresh)

## PS
- 현재 gateway를 직접 구현하다 보니 회원 서비스와 인증 서비스간의 연결이 되어 있지 않은 상태입니다. 양해 부탁드립니다😢
- 일단 spring 라이브러리를 이용해 연결해놓고 추가 구현을 하겠습니다.

# 질문
### 1. 공통되는 모듈 구현
MSA 환경을 위해 모놀리틱 방식을 사용하지 않다 보니 공통되는 기능을 중복적으로 구현해야 하는 상황이 발생했습니다.  
(예시: User-Service, Auth-Server 모두 User Persistence, Redis API를 구현해야 함.)  
버전 업 시 모두 수정을 해야 하는 상황이 발생하는데(관리 포인트가 2배) 이런 경우 어떻게 하는 것이 좋을까요?

### 2. gateway도 failover가 가능한가요?
gateway를 통해 접속하는 여러 서비스들은 failover가 가능하다는 것을 알게 되었는데 정작 gateway는 SPOF를 피해갈 수 없는 것 같습니다.
