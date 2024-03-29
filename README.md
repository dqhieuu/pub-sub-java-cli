# pub-sub-java-cli
Bộ 3 chương trình sử dụng 1 giao thức tương tự MQTT. Mô phỏng 1 hệ thống smart home với 3 thành phần:

- **Subscriber**: Phần mềm theo dõi thông tin smart home
- **Publisher**: Phần mềm giả lập sinh dữ liệu cảm biến
- **Broker**: Server phân phối message gửi giữa các publisher, subscriber. Sử dụng 1 hàng chờ để xử lý tuần tự, thread-safe (dựa trên pattern Message queue)

Trong repo này gồm 5 folder
- `broker` chứa mã nguồn ứng dụng broker, giao diện dòng lệnh
- `javafxpub` chứa mã nguồn ứng dụng publisher, giao diện đồ hoạ
- `javafxsub` chứa mã nguồn ứng dụng subscriber, giao diện đồ hoạ
- `publisher` và `subscriber` chứa mã nguồn cũ của pub và sub, giao diện dòng lệnh (CLI)

Mô tả giao thức: https://docs.google.com/document/d/1mnQiARtPLRhIgPCVo_Ezg0cnaaAnJfK_KQo0pJC4-HM/edit?usp=sharing

## Cài đặt
### Lưu ý
Ứng dụng yêu cầu compile và chạy với phiên bản **Java >= 17.0**

### Đối với 2 ứng dụng GUI
Để build ứng dụng, với 2 source code javafx của publisher và subscriber, chạy lệnh

`gradlew jar`

Tại thư mục `./build/libs/` được sinh ra, copy thư viện javafx ứng với hệ điều hành cần chạy vào đây (tải tại https://gluonhq.com/products/javafx/ rồi copy nội dung trong thư mục `javafx-sdk-*` trong file `.zip` vào thư mục chứa file jar / hoặc download phiên bản nhẹ hỗ trợ đa nền tảng tại https://files.catbox.moe/r80pe1.zip) và chạy:

`java -jar --module-path lib --add-modules javafx.controls,javafx.fxml <tên_file_jar>.jar`

### Đối với các ứng dụng CLI còn lại
**Cách 1:** Sử dụng IDE/text editor có hỗ trợ compile và chạy code java, rồi chạy với entry là tệp chứa hàm `main` trong thư mục đó.

**Cách 2:** Sử dụng compiler của java, include tất cả các file `.java` trong thư mục vào, với entry là file chứa hàm main để build ra 1 file `.jar`

**Cách 3:** Tạo mới 1 project gradle/maven, copy các file mã nguồn trong folder vào. Trong file config nhập entry là thư mục chứa hàm main. Tạo 1 command để build jar/fatjar (tham khảo trên trang docs của build tool tương ứng)

## Phân công công việc
### Phần giao diện/ tính năng CLI
- Đào Quang Hiếu & Đỗ Hồng Hà làm phần broker, collab trên Code with me
- Đỗ Hồng Hà làm phần publisher
- Lê Vũ Quang làm subsciber

### Phần giao diện / tính năng GUI
- Đào Quang Hiếu làm giao diện subscriber (ứng dụng quản lý smart home)
- Đỗ Hồng Hà làm giao diện publisher (ứng dụng giả lập các thiết bị smart home)

## Hình ảnh
![image](https://user-images.githubusercontent.com/63568218/146670351-73aa8939-b98e-4c6a-b36a-c8e0c20ccf11.png)

![image](https://user-images.githubusercontent.com/63568218/146670355-2afa7908-7096-4a2a-9999-633df9cbea71.png)

![image](https://user-images.githubusercontent.com/63568218/146670359-76d42377-05cb-4adf-93d8-cdd58dfc7e7e.png)

![image](https://user-images.githubusercontent.com/63568218/146670364-78393d4f-f926-4ed6-ab7d-e703bf7d1a27.png)

![image](https://user-images.githubusercontent.com/63568218/146670371-10a6c1a4-e544-4668-95e8-258ee5a6af98.png)

![image](https://user-images.githubusercontent.com/63568218/146670375-d1d4af7b-98cc-4da4-a84c-ea34cb845b57.png)
