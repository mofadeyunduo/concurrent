# IO

## Java

### File

- 处理文件、目录

### FileNameFilter

- 过滤文件名

### InputStream

- 字节流
- 输入，包括字节数组、字符串、文件、管道、流、其他数据源，包括 ByteArrayInputStream、StringBufferInputStream、FileInputStream、PipedInputStream、SequenceInputStream（合并两个 InputStream）
- FilterInputStream 修改了内部的行为，或者是返回对象的方式，包括 DataInputStream、BuffedInputStream、LineNumberInputStream、PushbackInputStream
- DataInputStream 读取数据，必须通过 DataOutputStream 写入数据，专用于 JVM 平台

### OutputStream

- 字节流
- 输出，包括 ByteArrayOutputStream、FileOutputStream、PipedOutputStream
- FilterOutputStream 修改了写入对象的方式，包括 DataOutputStream、PrintStream、BufferedOutputStream
- DataOutputStream 写入数据，必须通过 DataInputStream 读取数据，专用于 JVM 平台

### Reader

- 字符流
- 需要用 InputStreamReader 将 InputStream 转换
- 主要是为了国际化
- XXXInputStream 对应 XXXReader

### Writer

- 字符流
- 需要用 OutputStreamReader 将 OutputStream 转换
- 主要是为了国际化
- XXXOutputStream 对应 XXXWriter

### RandomAccessFile

- 随机读取
- seek 调到文件某一个地方

### Scanner 

- 读取器，传入 InputStream

### System.XXX

- in 输入、out 输出、err 错误
- setXXX 重定向

### ProcessBuilder

- 进程控制