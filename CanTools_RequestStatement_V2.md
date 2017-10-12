# 需求分析02次
### 1.软件定义</br>
#### 1.1问题定义  
- 汽车使用电子控制装置（ECU：Electronic Control Unit）对车进行控制
- ECU信息交换依赖CAN总线网络连接
- CAN总线检测CanBus信息，CanTool装置检测CAN总线信息，CanTool装置信息与上位机进行交互。目前，可以通过三种方式对上位机进行数据管理，包括：A.IOS 	B.Android 	C.PC程序
- 本程序采用Android进行程序的设计以及实现。</br>
#### 1.2软件体系结构
 ![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/1.png)</br>
###### 图1-2  CanTool系统框图
### 2.需求分析
#### 2.1模型选择
 ![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/2.png)
</br> 图2-1 快速原型模型
##### 利用快速原型模型的好处:
- 能够尽快实现功能原型，便于使用者的进一步的使用。
- 便于开发者以及使用者的不断沟通，以便于进一步地实现功能以供开发者的进一步开发以及用户的进一步确认需求
- 更加灵活
- 能够便于开发者的学习，提供更加充足的时间进行学习。 </br>
#### 2.2框图选择
##### 2.2.1 Android App功能结构设计
![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/3.png)
</br>图2-2-1 Android App业务框图
###### 收发器的功能:
- 接受数据
- 发送数据
###### 信息接收的格式问题&emsp;[标志位][ID][DLC][DATA]</br>
- [标志位] :
  - t:标准帧——其后的ID位数为3位
  - T:扩展帧——其后的ID位数为8位
- [ID] : 
  - ID编号，由硬件设备传输
    - 当t时，位数为3位(000~FFF)
    - 当T时，位数为8位(00000000~FFFFFFFF)
- [DLC]：
  - 传输的DATA数据的长度
- [DATA]：
  - 传输的数据的实际内容
  - 最多八个字节
###### 知识点补充:
例如:0x  XX  XX  XX  XX  XX  XX  XX 	 XX
- 如上例所示，为16进制的传输数据，从右往左读
- 本传输数据中是由多个0x XX组成的数据故，数据需要从左往右读
- 1个字节=2个16进制字符，1个16进制字符=0.5个字节；

##### 2.2.2 数据解析解析框图设计

![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/4.png)</br>
图2-2-2 数据解析框图</br>
###### 其中，信息描述数据库中的表单有两个：
- Can信息表
- Can信号表</br>

###### 注意：</br>
&emsp;&emsp;其中，CAN信息表中的ID编号是唯一确定的对应于CAN信息的ID编号，CAN信息的ID编号为16进制，CAN信息表中的ID为10进制，2者需要一个转换;</br>
&emsp;&emsp;每个CAN信息中的ID编号对应多条CAN信号表的内容;
##### 2.2.3 整体数据流图设计
![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/5.png)</br>
图2-2-3 Android App数据流图

##### 2.2.4 整体功能模块设计
 ![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/6.png)</br>
图2-2-4 整体功能模块图
### 3.概要设计
#### 3.1模型结构模拟
##### 3.1.1数据传输
###### （1）工作流程
 ![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/7.png)</br>
图3-1-1-1	数据模块的收发模型图
![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/8.png)</br>
图3-1-1-2	数据模块的收发流程图
###### （2）功能原理
![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/9.png)</br>
图3-1-1-3	蓝牙模块原理图
##### 3.1.2处理模块
![image](https://raw.githubusercontent.com/LeeYuxuan1104/HelloWorld/master/Pics/RS_Pics/10.png)</br>
图3-1-2 数据处理的业务流程图
