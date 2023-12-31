# Inventory-Management-Android

## Kiên trúc ứng dụng
- Sử dụng kiến trúc MVC (Model - View - Controller)

- Các lớp Controller: 
    + LoginController: xử lý logic đăng nhập sử dụng firebase authentication service
    + RegisterController: xử lý logic đăng ký sử dụng firebase authentication service
    + ManageItem: bao gồm các controller xử lý cho module quản lý sản phẩm (AddItemController, EditItemController, DeleteItemController. ScanCodeController)
    + ImportExport: bao gồm các controller xử lý cho module nhập xuất sản phẩm từ tệp dữ liệu (ImportController, ExportController)

- Các lớp Model: 
    + Item: đại diện cho một sản phẩm
    + User: đại diện cho một người dùng
    + Storage: đại diện cho một kho hàng
    + UserStorage: lớp liên kết giữa User và Storage
    + ItemStorage: lớp liên kết giữa Item và Storage

- Các lớp View: bao gồm các activity, fragment, adapter
    + LoginActivity
    + RegisterActivity
    + ImportActivity
    + ExportActivity
    + AddItemActivity
    + DeleteItemActivity
    + ScanCodeActivity
    + EditItemActivity
    + viewInventoryActivity
    + MainActivity
    + DashboardActivity

- Các lớp service: xử lý logic thao tác dữ liệu với db
  + ManageItemService: xử lý logic cho module quản lý sản phẩm
  + GoogleSheetService: thực hiện tương tác với google sheet api

