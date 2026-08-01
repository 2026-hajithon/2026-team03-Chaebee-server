채비 Server

여행 준비 과정을 도와주는 서비스의 백엔드 서버입니다. 여행 등록, D-day 기반 맞춤 정보 제공(에디터의 발견) 등 여행 전 준비 과정을 지원합니다.

주요 기능
여행 등록: 예정된 여행 일정을 등록하고 관리
에디터의 발견: 여행 D-day에 맞춰 필요한 정보(환전, 여행자 패스, 마일리지 등)를 D-day 그룹별로 제공
알림: 여행 준비 관련 알림 발송
기술 스택
분류	스택
Language	Java 21
Framework	Spring Boot, Spring Security
DB	MySQL, JPA
Auth	JWT
Cache	Redis
Infra	AWS Elastic Beanstalk, RDS, S3
CI/CD	GitHub Actions
src/main/java/com/[패키지명]
├── domain
│   ├── trip           # 여행 등록/관리
│   └── discovery       # 에디터의 발견 (D-day 기반 콘텐츠)
├── global
│   ├── auth            # JWT 인증
│   ├── config
│   └── exception        # ProjectException / BaseErrorCode
└── ...