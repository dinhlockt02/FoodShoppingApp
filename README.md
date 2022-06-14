# Phần mềm đặt đồ ăn trực tuyến
## Đặt vấn đề
 - Ngày nay, việc đặt đồ ăn thông qua Internet diễn ra rất phổ biến, đặc biệt sau đại dịch Covid-19, mọi người đã quen với việc đặt đồ ăn ở trên mạng, thông qua các ứng dụng đặt đồ ăn như GrabFood, Baemin, … Tuy nhiên, việc đặt đồ ăn qua các nền tảng có một số bất cập như cửa hàng phụ thuộc vào dịch vụ giao hàng của các nền tảng này, không tự chủ việc giao đồ ăn, phụ thuộc vào hệ thống thanh toán của nền tảng, ví như Grabfood cần sử dụng ví Moca. Ngoài ra, các nền tảng này có chi phí trung gian cao và có xu hướng phát triển ở vùng đô thị lớn, nên một số vùng ngoại tỉnh, hoặc xa trung tâm như làng đại học, các tỉnh và miền quê, việc đặt và giao đồ ăn chủ yếu vẫn thông qua mạng xã hội hoặc điện thoại.
 - Từ những vấn đề trên, nhóm thực hiện đề tài với mong muốn xây dựng một ứng dụng đặt đồ ăn mang nhiều quyền tự chủ hơn cho cửa hàng, đồng thời hướng tới đối tượng đặt đồ ăn là sinh viên, đặc biệt là sinh viên ở làng đại học, khi việc đặt đồ ăn chủ yếu thông qua gọi điện thoại, mạng xã hội … dẫn đến việc đặt đồ ăn rất khó khăn và không thể quản lý. Ứng dụng sẽ đứng dưới vai trò như một chợ tập trung và là trung gian nhằm kết nối người mua và cửa hàng. Thông qua ứng dụng, người dùng có thể tìm kiếm nhiều cửa hàng ở xung quanh mình và cửa hàng cũng sẽ dễ dàng tiếp cận khách hàng hơn, tự chủ trong việc thanh toán và giao hàng.
## Chức năng ứng dụng
### Đăng nhập
- <img src=https://github.com/ShShee/thiBangLai-A1/blob/main/giaoDienThucHien/trangChu.jpg width=300 height=580 />
### Đăng ký
### Quên mật khẩu
### Màn hình chính
- Home
  - <img src=https://github.com/ShShee/thiBangLai-A1/blob/main/giaoDienThucHien/traCuu.jpg width=300 height=580 />
- Orders
  - <img src=https://github.com/ShShee/thiBangLai-A1/blob/main/giaoDienThucHien/bienBao.png width=601 height=580 />
- Saved
  - <img src=https://github.com/ShShee/thiBangLai-A1/blob/main/giaoDienThucHien/sahinh.png width=300 height=580 />
- Bag
  - <img src=https://github.com/ShShee/thiBangLai-A1/blob/main/giaoDienThucHien/phapluat.png width=300 height=580 />
### Tìm kiếm cửa hàng
### Danh sách cửa hàng theo sự kiện hiện có
### Danh sách danh mục sản phẩm
### Danh sách cửa hàng theo bộ lọc

## Yêu cầu thiết bị
- Android
  - minSdkVersion: 21.0
  - targetSdkVersion: 29.0
  - Gradle: 3.5.3
- IOS
  - ios: 11.0
## Công nghệ và thư viện
- Công nghệ
  - Ngôn ngữ: Javascript
  - Framework Frontend: [React Native](https://github.com/facebook/react-native)
  - Framework UI: [UI Kittens](https://akveo.github.io/react-native-ui-kitten/docs/guides/getting-started#getting-started)
  - Backend Platform: [Node.js](https://nodejs.org/en/)
  - Database: [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) và [Expo SQLite](https://docs.expo.io/versions/latest/sdk/sqlite/)
- Thư viện
  - [Expo](https://github.com/expo/expo)
  - [React Navigation](https://reactnavigation.org/)
  - [react-native-swiper](https://github.com/leecade/react-native-swiper)
  - [Redux](https://github.com/reduxjs/redux-toolkit)
  - [react-native-progress](https://github.com/react-native-progress-view/progress-bar-android)
  - [react-native-modals](https://github.com/jacklam718/react-native-modals)
  - [react-countdown-circle-timer](https://github.com/vydimitrov/react-countdown-circle-timer/tree/master/packages/mobile)
  - [react-native-indicators](https://github.com/n4kz/react-native-indicators)
  - [react-native-fast-toast](https://github.com/arnnis/react-native-fast-toast)
  - [sharingan-rn-modal-dropdown](https://github.com/srk-sharingan/sharingan-rn-modal-dropdown)
  - [react-native-admob](https://github.com/sbugert/react-native-admob)
## Backend
Source code: [ThiGPLX-Backend](https://github.com/ShShee/ThiGPLX-Backend)
## Database
<img src=https://github.com/ShShee/thiBangLai-A1/blob/main/giaoDienThucHien/database.PNG width=927 height=258 />

## Sơ đồ kiến trúc
<img src=https://github.com/ShShee/thiBangLai-A1/blob/main/giaoDienThucHien/Untitled%20Diagram.png width=608 height=287 />

## License
Licensed under the [MIT License](LICENSE)
