#  MusicServer
## 简介

本项目是适配本人[IOS音乐应用](https://github.com/TaopiTTT/MyCloudMusic)的后端服务器，提供账户登陆认证，广告、歌单以及音乐播放接口

## 项目配置环境（提供本地环境作为参考）

- 机器为Macbook Air (m1)

- nginx-1.29.4

- IntelliJ IDEA 2025.2.5

- Java JDK-21

- Maven 3.9.9
- MySQL 8.0.44



## 配置流程

### 接口分发

由于音乐项目中与后端的通信大部分来自/v1/xxx接口，而资源的访问却是来自/assets/xxx接口，因此我们需要先使用Nginx进行接口分发，配置文件如下：

```nginx
server {
    listen 9178;
    server_name localhost;

    # 静态资源目录 - 把 "UserID" 改成实际用户名
    root /Users/{UserID}/MusicSever;

    # 静态资源
    location /assets/ {
        autoindex on;
        add_header Access-Control-Allow-Origin *;
        add_header Access-Control-Allow-Methods 'GET, OPTIONS';
        expires 7d;
    }

    # API 请求转发到你的 8088 后端
    location /v1/ {
        proxy_pass http://127.0.0.1:8088;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

配置好后启动Nginx即可。记得在音乐项目中修改Config中的ENDPOINT和RESOURCE_ENDPOINT配置（本人是对局域网开放，用另一台mac连接，如果音乐项目与后端开在一台设备上则可以使用localhost:9178进行访问）<br>

### 部署项目

选择合适的位置克隆本项目

```bash
git clone https://github.com/TaopiTTT/MusicServer.git
```

使用IntelliJ IDEA打开项目文件夹，用Maven编译项目运行即可<br>

请注意电脑防火墙是否开放！本项目将用到8088、9178端口，请确保以上端口并未被占用。

## 参考资源

可在本仓库的assets文件夹下获取。
