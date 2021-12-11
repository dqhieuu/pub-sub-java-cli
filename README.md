# pub-sub-java-cli
Bộ 3 chương trình sử dụng 1 giao thức tương tự MQTT. Mô phỏng 1 hệ thống smart home với 3 thành phần:

Subscriber: Phần mềm theo dõi thông tin smart home
Publisher: Phần mềm giả lập sinh dữ liệu cảm biến
Broker: Server phân phối message gửi giữa các publisher, subscriber. Sử dụng 1 hàng chờ xử lý tuần tự, thread-safe (dựa trên pattern Message queue)
