# README

# 개인화 식물 재배 및 관리 시스템 구축

### 아두이노 / 자바 스프링 / 리액트를 이용한 사용자 맞춤 반려식물 성장 서비스




## 🖥️ 프로젝트 소개

### 사용자의 환경에 맞춰 식물의 성장을 보조하는 플랫폼

### ✔ 주요 기능

- 키트에 탑재된 센서를 통해 해당 키트에 등록된 식물에게 필요한 정보들을 주변 환경을 고려하여 제공
- 원격에서 센서를 조작하여 빛 조절 또는 물 주기 가능
- 식물을 키우면서 관리할 수 있는 다이어리를 통해 사진과 기록을 남길 수 있음
- AI 이미지 분류를 통한 식물 검색 기능 제공

### 🕰️ 개발기간

- 23.07.24. - 23.08.23.

### 🧑‍🤝‍🧑 맴버구성

- 팀장 : 정균민 - 백엔드, 센서
- 팀원 : 김지성 - 프론트엔드, AI

### ⚙️ 기술 스텍

### Backend

<img src="https://img.shields.io/badge/java-000000?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white"> <img src="https://img.shields.io/badge/jsonwebtokens-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">

### Frontend

<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white"> <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white"> <img src="https://img.shields.io/badge/redux-764ABC?style=for-the-badge&logo=redux&logoColor=white"> <img src="https://img.shields.io/badge/reactrouter-CA4245?style=for-the-badge&logo=reactrouter&logoColor=white"> <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">

### AI

<img src="https://img.shields.io/badge/python-3776AB?style=for-the-badge&logo=python&logoColor=white"> <img src="https://img.shields.io/badge/flask-000000?style=for-the-badge&logo=flask&logoColor=white"> <img src="https://img.shields.io/badge/tensorflow-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white"> <img src="https://img.shields.io/badge/keras-D00000?style=for-the-badge&logo=keras&logoColor=white"> <img src="https://img.shields.io/badge/selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white">

### Sensor

<img src="https://img.shields.io/badge/arduino-00878F?style=for-the-badge&logo=arduino&logoColor=white"> <img src="https://img.shields.io/badge/mqtt-660066?style=for-the-badge&logo=mqtt&logoColor=white"> <img src="https://img.shields.io/badge/adafruit-000000?style=for-the-badge&logo=adafruit&logoColor=white">

### Deploy

<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/netlify-00C7B7?style=for-the-badge&logo=netlify&logoColor=white">

## Architecture
![architecture](./image/architecture.png)
## API Doc
![API Doc](image/Screenshot%202023-08-23%20at%2012.08.43.JPG)
## Class Diagram
# Project ER Diagram
```mermaid

erDiagram
  user ||--o{ arduino_kit : has
  user ||--o{ previous_plants : has
  arduino_kit ||--o{ plant : related
  arduino_kit ||--|{ SensorData : contains
  arduino_kit ||--o{ plant_watering : contains
	arduino_kit ||--|| Released_kit : consist
  plant ||--o| previous_plants : related
  plant ||--o{ plant_picture : has 
  plant_picture }o--o{ user : has
  
  
  user {
    bigint(20) user_seq_num PK
    varchar(255) user_email
		varchar(10) user_id
    varchar(3) user_name
    varchar(50) user_nickname
    varchar(255) user_password 
    varchar(50) user_phone_num
		tinyint(4)	authority
  }

  arduino_kit {
    varchar(100) kit_no PK,FK
		bigint(20) member_seq_num FK
    datetime(6) date
    varchar(255) serial_num
    varchar(255) deviceName
  }
	
	Released_kit{
		bigint(20) release_num PK
		text kit_serial_num FK
	}

	previous_plants{
    bigint(20) reg_no PK
    bigint(20) user_seq_num FK
    bigint(20) plant FK
    datetime(6) plant_harvest_date
  }

  plant {
    bigint(20) plant_num PK
	bigint(20) plant_kit_no FK
    varchar(255) plant_name
    datetime plant_reg_date
  }

  SensorData {
    bigint(20) id PK
    bigint(20) kit_no FK
    double humidity
    double temp
    int(11) illuminance
    double soil_moisture
    DATETIME(6) date
  }

   plant_watering{
    bigint(20) watering_no PK
    datetime(6) watering_date
    bigint(20) kit_no FK
   }
   plant_picture{
    bigint(20) id PK
    datetime(6) date
    varchar(255) msg
    varchar(255) org_file_name
    varchar(255) stored_path
    bigint(20) user_seq_num FK
    bigint(20) plant_num FK
   }
```
## UML Diagram
# Project UML Class Diagram
```mermaid
classDiagram
    class User {
    -id : String
    -pw : String
	-nickname : String
	-email : String
	-phonenum : String
	-name : String
    -arduinoKits : ArduinoKit[*]
    -userPlants : UserPlants[*]
    -Authority : userRole
    }

    class AuthService{
        +join()
        +login()
        +findId()
        +findPw()
    }

    class AuthController{
        -authService : AuthService
        -sendMailService : SendMailService
    
    }

    class TokenProvider {
        -AUTHORITIES_Key : String
        -key : Key
        -BEARER_TYPE : String
        -ACCESS_TOKEN_EXPIRE_TIME : long
        +generateTokenDto(authentication :Authentication)
        +getAuthentication(accessToken : String)
        +validateToken(toke : String)
        parseClaims(accessToken : String)
    }

    class ArduinoKit {
        -serial_num : String
        -registerDate : String
        -qr_code : String
        -deviceName : String
    }

    class ReleasedKit{
        ReleaseNum : Long
        KitSerialNum : Long
        arduinoKit : ArduinoKit
    }

    class previousPlant {
        -reg_no : long
        -userSeqNum : long
        -plant : Plant
        -plant_harvest_date : String
    }

    class Plant {
        -PlantNo : String
        -PlantName : String
        -PlantState : char
        -PlantRegDate : String
    }

    class PlantSystemWeb {
        -myPlants : Plant[*]
        -myPreviousPlant : previousPlant[*]
        -myKits : Kits[*]
        +login()
        +logout()
        +addArduinoKit(arduinoKit)
        +addPlant(Plant)
        +endGrowthPlant(arduinoKit)
        +getSensorData()
        +addPlant(Plant)
        +controlBrightness()
        +supplyWaterToPlant()
    }

    class KitController{
        -kitAndPlantManageService : KitAndPlantManageService
    }

    class SensorController{

    }

    class KitAndPlantManageService {
        -arduinoKitService : ArduinoKitService
        -releasedKitService : ReleasedKitService
        -plantservice : PlantService
        -mqttReceiver : MqttReceiver
        -plantWaterService : PlantWaterService
        -plantPictureService : PlantPictureService
        -command : Map<String, String>
        -MyGateWay : myGateWay
        +validateKit(serial_num : String)
        +addKit(arduinoKit)
        +deleteKit(kitNo : long)
        +plantingPlant(kitNo : long)
        +controlBrightnessInternal()
        +supplyWater()
        +sendToMqtt(TOPIC : String, command : String)
    }

    class ETboard{
        -AP_SSID : String
        -AP_PASSWORD : String
        -switchStatus : boolean
        -hasPlant : boolean
        -kitNo : long
        -temp : double
        -humidity : double
        -soil_moisture : int
        +sendSensorData(jsonStr : String)
        +MqttConnect()
        +cmdCallback(order : const char *)
        +calcCelcious()
        +startAP()
        +serverInitialConfigPage()
        +saveConfigHandler(request : AsyncWebServerRequest *)
        +loadConfig()
        +attemptWiFiConnection()
    }

    class MyGateWay{
        <<interface>>
        +sendToMqtt()
    }

    class Flask_ai_server {
        +predict(PlantImg)
    }

    User "1" *-- "N" ArduinoKit : has
    User "1" *-- "N" previousPlant : has
    User "1" ..> "1" PlantSystemWeb : uses
    Plant "1" o-- "N" previousPlant : related
    ArduinoKit "1" o-- "1" Plant : related
    ArduinoKit "1" *-- "1" ETboard : consist
    ArduinoKit "1" --* "1" ReleasedKit : consist
    PlantSystemWeb ..> KitController : uses
    PlantSystemWeb ..> AuthController : uses
    PlantSystemWeb ..> SensorController : uses
    PlantSystemWeb ..> UserController : uses
    AuthController ..> AuthService : uses
    KitController ..> KitAndPlantManageService : uses
    KitAndPlantManageService ..> MyGateWay : uses
    PlantSystemWeb ..> Flask_ai_server : uses
    MyGateWay ..> ETboard : sends
    AuthService ..> TokenProvider : uses
    KitAndPlantManageService o-- ArduinoKit : controls
```
## User flowchart
![flow_chart](./image/flow%20chart.png)
