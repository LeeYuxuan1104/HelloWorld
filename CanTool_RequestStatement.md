##CanTool Application需求文档##
**02组完成Android应用的功能任务**
###一、软件作用###
- 实现CAN数据的显示和控制
###二、功能划分###
- COM接口管理
	- 1.搜索本机接口
	- 2.COM接口列表显示
	- 3.COM值得设置
		- A.波特率设置
		- B.数据位设置
		- C.停止位数设置
	- 4.COM设置的保存
- CAN信息的设定
	- 1.CAN物理信号值设定
	- 2.CAN速率设置
	- 3.进入CAN工作状态(Open)
	- 4.CAN状态初始化(Close)
	- 5.设置信息保存
	- 6.CAN信息发送周期设定
- CAN信息接收
	- 1.CAN信息的显示
		- A.CAN信息原始数据的显示
			- 仪表形式显示
		- B.CAN信号物理值实时数据显示
			- 物理值曲线行驶显示
	- 2.CAN信息保存
		- 格式CSV形式保存
	- 3.信号转换
		- 物理值与CAN信号值之间的转化
			- A.物理值转化为CAN信号值
			- B.CAN信号值转化为物理值
	- 4.CAN信息和信号数据库信息管理
		- A.信息的加载
		- B.CAN信息解析
		- C.信息的显示
			- 树状信息显示
		- D.信息的保存
		- E.信息转换
			- ①数据库信息转化成为XML和JSON形式
			- ②XML和JSON形式数据转化为数据库信息
- 信息同步   