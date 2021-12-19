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

## Phân công công việc
### Phần giao diện/ tính năng CLI
- Đào Quang Hiếu & Đỗ Hồng Hà làm phần broker, collab trên Code with me
- Đỗ Hồng Hà làm phần publisher
- Lê Vũ Quang làm subsciber

### Phần giao diện / tính năng GUI
- Đào Quang Hiếu làm giao diện subscriber (ứng dụng quản lý smart home)
- Đỗ Hồng Hà làm giao diện publisher (ứng dụng giả lập các thiết bị smart home)

## Hình ảnh
