# Study-Junit
자바와 JUnit을 활용한 실용주의 단위 테스트(제프 랭어, 앤디 헌트, 데이브 토마스 지음) 소스

## [Settings]
### Java
- zulu 11 jdk
### gradle
- 5.2.1

--- 

## FIRST

### FIRST: 좋은 테스트의 속성(조건)

- F(fast: 빠른)
- I(isolated: 고립된)
- R(repeatable: 반복 가능한)
- S(self-validating: 스스로 검증 가능한)
- T(timely: 적시의)

## [Right]-BICEP

### [Right]-BICEP : 무엇을 테스트 할 것인가?

- Right : 결과가 올바른가?
- B : 경계 조건(boundary conditions)은 맞는가?
  - 고려해야 할 경계 조건
    - 모호하고 일관성 없는 입력 값 (예: 특수 문자가 포함된 파일이름)
    - 잘못된 양식의 데이터 (예: 최상위 도메인이 빠진 메일주소)
    - 수치적 오버플로를 일으키는 계산
    - 비거나 빠진 값 (예: 0.0, "", null)
    - 이성적인 기대값을 훨씬 벗어나는 값 (예: 150세의 나이)
    - 교실의 당번표처럼 중복을 허용해서는 안되는 목록에 중복 값이 있는 경우
    - 정렬이 안된 정렬 리스트 혹은 그 반대 (예: 정렬 알고리즘에 이미 정렬된 입력 값을 넣거나 정렬 알고리즘의 역순 데이터를 넣는 경우)
    - 시간 순이 맞지 않는 경우 (예: HTTP 서버가 OPTIONS 메소드의 결과를 POST 메소드보다 먼저 반환하지 않고 그 후에 반환하는 경우)
  - CORRECT (잠재적인 경계 조건)
    - [C] Conformance(준수) : 값이 기대한 양식을 준수하고 있는가?
    - [O] Ordering(순서) : 값의 집합이 적절하게 정렬 되거나 정렬되지 않았나?
    - [R] Range(범위) : 이성적인 최솟값과 최대값 안에 있는가?
    - [R] Reference(참조) : 코드 자체에서 통제할 수 없는 어떤 외부 참조를 포함하고 있는가?
    - [E] Existence(존재) : 값이 존재하는가? (non null, non zero, 집합에 존재하는가?)
    - [C] Cardinality(기수) : 정확히 충분한 값들이 있는가?
    - [T] Time(절대적, 상대적 시간) : 모든 것이 순서대로 일어나는가? 정확한 시간에? 정시에?
- I : 역 관계(inverse relationship)를 검사할 수 있는가?
- C : 다른 수단을 활용하여 교차 검사(cross-check)를 할 수 있는가?
- E : 오류 조건(error conditions)을 강제로 일어나게 할 수 있는가?
  - 고려해야 할 제약 사항
    - 메모리가 가득 찰 때
    - 디스크 공간이 가득 찰 때
    - 벽시계 시간에 관한 문제들 (서버와 클라이언트 간 시간이 달라서 발생하는 문제들)
    - 네트워크 가용성 및 오류들
    - 시스템 로드
    - 제한된 색상 팔레트
    - 매우 높거나 낮은 비디오 해상도
- P : 성능 조건(performance characteristics)은 기준에 부합하는가?
  - 주의 사항
    - 전형적으로 코드 덩어리를 충분한 횟수만큼 실행하길 원할 것.  
      이렇게 타이밍과 CPU 클록 주기에 관한 이슈를 제거
    - 반복하는 코드 부분을 자바(JVM)가 최적화하지 못하는지 확인해야 함
    - 최적화되지 않은 테스트는 한 번에 수 밀리초가 걸리는 일반적인 테스트 코드보다 매우 느림.  
      느린 테스트들은 빠른 것과 분리할 것.
    - 동일한 머신이라도 실행 시간은 시스템 로드처럼 잡다한 요소에 따라 달라질 수 있음
  - 도구
    - jmeter
    - JUnitPerf
    
## 경게 조건 CORRECT

### CORRECT 기억법

- [C] Conformance(준수)
   - 이메일 주소, 전화번호, 계좌 번호, 파일 이름 등 양식 있는 문자열 데이터 등을 검증할 때는 많은 규칙이 필요
   - 이러한 규칙들을 잘 준수하고 있는지, 명세를 잘 설계할 것
- [O] Ordering(순서)
   - 순서 조건 확인
   - 예 : compareTo() 메소드처럼 순서에 따라 다른 결과값이 나오는 경우
- [R] Range(범위)
   - 인덱스를 다룰 때 고려해야 할 테스트 시나리오
     - 시작과 마지막 인덱스가 같으면 안됨
     - 시작이 마지막보다 크면 안됨
     - 인덱스는 음수가 아니어야 함
     - 인덱스가 허용한 것보다 크면 안됨
     - 개수가 실제 항목 개수와 맞아야 함
- [R] Reference(참조)
   - 메소드 테스트 시 고려 사항
     - 범위를 넘어서는 것을 참조하고 있지 않은지
     - 외부 의존성은 무엇인지
     - 특정 상태에 있는 객체를 의존하고 있는지 여부
     - 반드시 존재해야 하는 그 외 다른 조건들
- [E] Existence(존재)
   - 주어진 값이 존재하는지
   - 막혀 있는 곳(?)에 null 값이 도달한다면 문제 원인을 이해하기 어려움
   - 호출된 메소드가 null을 반환하거나 기대하는 파일이 없거나 네크워크가 다운되었을 때 발생되는 경우에 대한 테스트를 작성할 것
- [C] Cardinality(기수)
   - 울타리 기둥 오류(fencepost errors) : 충분히 생각하지 않아 발생하는 오류
   - 0-1-n 법칙 (ZOM) 테스트는 0, 1, n 이라는 경계 조건에만 집중 (n은 비즈니스 요구사항에 따라 변경될 수 있음)
- [T] Time(시간)
   - 상대적 시간(시간 순서)
     - login-logout, open-read-close처럼 메소드 호출 순서
     - 데이터의 순서가 중요한 것처럼 메소드의 호출 순서도 중요함
     - 타임아웃 문제도 포함됨
       - 타임아웃으로 보호되지 않는 조건 찾아보기
       - 발생하지 않을 일을 기다리느라 코드가 무한 대기에 빠지지 않았는지
       - 대기 시간이 너무 길지는 않은지 
   - 절대적 시간(측정된 시간)
     - 예 : UTC, DST에 따라 하루의 시간이 달라짐
   - 동시성 문제들
     - 동시성과 동기화된 접근 맥락
     - 동시에 같은 객체를 다수의 스레드가 접근할 때 발생하는 일
     - 어떤 전역 또는 인스턴스 수준의 데이터나 메소드에 동기화를 해야 하는지
     - 파일 또는 하드웨어의 외적인 접근 처리
     - 클라이언트에 동시성 요구 사항이 존재할 시 다수의 클라이언트 스레디를 보여주는 테스트를 작성할 필요가 있음

#### 불변식, 불변 
- 불변식(invariant)
  - 프로그램이 실행되는 동안 또는 정해진 기간 동안 반드시 만족해야 하는 조건을 의미
  - 변경을 허용하나 주어진 조건 내에서만 변경을 허용함
- 불변(immutable)
  - 어떠한 변경도 허용하지 않는 것
  - 가변 객체와 구분하는 용도로 사용

