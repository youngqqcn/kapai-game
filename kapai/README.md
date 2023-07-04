### 配置及服务

| 环境          | 说明                               |
|-------------|----------------------------------|
| 系统          | Centos7以上 或者 Ubuntu Server 22 以上 |
| 数据库         | Mysql7/8                         |
| Redis       | 6.x                              |
| JDK/OpenJDK | 17                               |
| Golang      | 1.18以上                           |
| 开发工具        | Intellij IDEA 2021 以上            |

### 源码编译及配置
**数据库初始化脚本`doc/db.sql`**

#### 服务配置`application.yml`
1. 数据库配置`spring.datasource`节点下
2. Redis配置`spring.data.redis`节点下
3. 系统私有配置在`app`节点下, 具体参数说明请查看配置文件

#### Java代码编译
1. 进入项目（kapai）根目录
2. Linux执行`./gradlew bootJar` Windows执行 `gradlew.bat bootJar`
3. 产物输出目录在 `./build/libs/**.jar`
4. nginx配置`doc/nginx.conf` 域名自行修改
5. 运行 `java -jar xx.jar`

***特别强调***
- **请保护好`/admin`相关接口**
- **请保护好`/admin`相关接口**
- **请保护好`/admin`相关接口**

**`/admin`下相关接口不应该对外暴露，应在nginx做限制**
```
    #禁止访问目录
    location ^~ /api/admin/ {
        deny all;
        allow IP;
    }

    #禁止访问目录
    location ^~ /admin/ {
        deny all;
        allow IP;
    }
```
配置后应该测试是否生效

#### Golang代码编译
1. 进入项目（kapai）golib目录
2. 执行 `go mod tidy` 下载依赖
3. 编译执行 `go build -buildmode=c-shared -o libweb3.so main.go`
4. 产物输出为 `libweb3.so`
5. 将静态库放到Java服务的`System.getProperty("user.dir") + "/libweb3.so"`位置。一般和jar在同一级目录，本地开发调试应该放在项目根目录

## 合约部署流程
1. 发行IOT合约`EPToken.sol`
2. 发行ART合约`AToken.sol`(需要先配置好钱包地址在发行)
关于IPancakeRouter02的合约地址，币安测试链使用`0xD99D1c33F9fC3444f8101754aBC46c52416550D1`，正式链使用`0x10ED43C718714eb63d5aA57B78B54704E256024E`
3. 发行SOUL合约`BToken.sol`(需要先配置好钱包地址在发行)
4. 发行节点人认购合约`RenGou.sol`(需要先配置好钱包地址和合约地址在发行,在代码里面修改中文处)
5. 发行每日分红合约`Release.sol`(需要先配置好钱包地址和合约地址在发行,在代码里面修改中文处) 构造参数startTime使用UTC时间8点
6. 从发行钱包向`RegGou.sol`的合约转`13743000`个ART 和 `???`个IOT
7. 从发行钱包向`Release.sol`的合约转`194257000`个ART 和 `20790`个SOUL
8. 在博饼创建ART(100万)/USDT(20万)和SOUL(210)/USDT(1万)的V2版本交易对，并获得底池地址。
9. 设置ART和SOUL代币合约的底池地址`setup`
10. SOUL设置可购买的钱包地址`addPassTo`

## 测试合约地址
USDT: `0x3966AEb2C687EA5F64440ea494E6aA7690FAC518`
IOT: `0x24d3502d7d744479f4030aE7163643Ae856ce324`
ART: `0x2fb579556F7553aeb1148Dc3BaDdE1bfd2Ca8D0e`
SOUL: `0x6247d35e6562dA66ACE5fc7c0AA3e413C0dadcc5`
认购合约: `0x52eB217C47d7490f581385787FDD258d1D21ca16`
每日释放合约: `0xd51FaBdC3c646d180E2EEfDdAdaB5763Ac678744`