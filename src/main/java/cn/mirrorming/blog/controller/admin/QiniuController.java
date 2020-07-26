package cn.mirrorming.blog.controller.admin;

import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.storage.Storage;
import cn.mirrorming.blog.exception.AppException;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.ImmutableMap;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 对七牛云对象储存的操作接口
 * 关于七牛云开放的Java-API文档请看：https://developer.qiniu.com/kodo/sdk/1662/java-sdk-6
 * 文件上传的DEMO请看 /test/java/cn/tycoding/UploadDemo.java 测试类。
 * 请先到七牛云个人控制中心查看Access key 和 Secret Key
 *
 * @author mireal
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("admin/qiniu")
public class QiniuController {
    //分布式ID生成器，用于设定上传文件的名称
    IdWorker idWorker = new IdWorker();
    /**
     * ACCESS_KEY和SECRET_KEY
     */
    private static final String ACCESS_KEY = "ojn8uVCpe7J1bMEQzIAqnXxk60qX_ak3taS-Mdas";
    private static final String SECRET_KEY = "Tb-d50uN8CNos25-QYaCtGJgXk4o28UIgsnk77xW";
    /**
     * 要上传的空间
     */
    private static final String BUCKETNAME = "public-mirrorblog";

    /**
     * 个人七牛云对象储存外链域名地址
     */
    private static final String URL = "http://media.mirrorming.cn/";
    private static final String SUFFIX = "imageView2/0/q/75|watermark/2/text/bWlycm9ybWluZy5jbg==/font/c2Vnb2Ugc2NyaXB0/fontsize/280/fill/IzAwMDAwMA==/dissolve/66/gravity/SouthEast/dx/10/dy/10|imageslim";

    /**
     * 获取七牛云个人储存空间域名地址
     *
     * @return
     */
    @GetMapping("/domain")
    public R domain() {
        return R.succeed(URL);
    }

    /**
     * 七牛云开放API接口：获取空间文件列表接口，详细的文档请看：https://developer.qiniu.com/kodo/sdk/1239/java#rs-list
     *
     * @return
     */
    @GetMapping("/list")
    public R list() {
        try {
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Region.autoRegion());
            //...其他参数参考类注释
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            //文件名前缀
            String prefix = "";
            //每次迭代的长度限制，最大1000，推荐值 1000
            int limit = 1000;
            //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            String delimiter = "";
            //列举空间文件列表
            BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(BUCKETNAME, prefix, limit, delimiter);
            List<Storage> list = new ArrayList<>();
            while (fileListIterator.hasNext()) {
                //处理获取的file list结果
                FileInfo[] items = fileListIterator.next();
                for (FileInfo item : items) {
                    Storage storage = Storage.builder()
                            .name(item.hash)
                            .size(item.fsize)
                            .type(item.mimeType)
                            .key(item.key)
                            .url(URL + item.key)
                            .build();
                    list.add(storage);
                }
            }
            return R.succeed(ImmutableMap.of("total", list.size(), "rows", list));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * 文件上传接口，调用七牛云开放的API
     *
     * @param file 上传的文件
     * @return 返回成功上传的文件：1.名称、2.外链地址
     * <p>
     * 注意：我们指定分布式ID生成器IdWorker工具类生成的随机数作为文件名称；
     * 外链：如果你是第一次使用七牛云，你需要先了解一下如何为自己的七牛云对象储存配置外链地址，因为官方提供的测试外链有时间限制
     * <p>
     */
    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.fail("上传文件不能为空");
        }
        //上传文件路径
        String filepath = "";
        //上传到七牛后保存的文件名
        String key = IdWorker.getId() + "";

        try {
            //将MutipartFile对象转换为File对象，相当于需要以本地作为缓冲区暂时储存文件
            //获取文件在服务器的储存位置
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filepath + file.getOriginalFilename());
            Files.write(path, bytes);

            //密钥配置
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
            Configuration c = new Configuration(Region.autoRegion());
            //创建上传对象
            UploadManager uploadManager = new UploadManager(c);
            //调用put方法上传
            Response res = uploadManager.put(bytes, key, auth.uploadToken(BUCKETNAME));
//                Response res = uploadManager.put(FilePath, key, auth.uploadToken(BUCKETNAME));
            //打印返回的信息
            //res.bodyString() 返回数据格式： {"hash":"FlHXdiArTIzeNy94EOxzlCQC7pDS","key":"1074213185631420416.png"}
            log.info("文件上传成功 =>" + res.bodyString() + "'" + URL + key + "'");

            return R.succeed(ImmutableMap.of("key", key, "url", URL + key + "?" + SUFFIX));
        } catch (Exception e) {
            log.error("文件上传失败============>" + e.getMessage());
            throw new AppException("文件上传失败");
        }
    }

    /**
     * 文件下载
     *
     * @param name 文件名称
     * @return 返回文件在七牛云储存的地址：外链/文件名  前端处理下载
     */
    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("name") String name, HttpServletResponse response) {
        try {
            String encodedFileName = URLEncoder.encode(name, "utf-8"); //获取文件名，防止乱码
            String fileUrl = String.format("%s%s", URL, encodedFileName); //拼接得到文件的连接地址
            //获取外部文件流
            URL url = new URL(fileUrl);
            //打开一个连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3 * 1000);

            //获取输入流
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //创建一个字节数组缓冲区对象
            byte[] buffer = new byte[1024];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                //三个参数：b:字节数组 off:起始位置 len:写入字节长度
                outputStream.write(buffer, 0, len); //将文件输入流中的字节一次read读取并写入到字节缓冲区对象ByteArrayOutputStream中
            }

            //设置请求头格式
            HttpHeaders headers = new HttpHeaders();
            //告诉浏览器以"attachment"方式打开文件
            headers.setContentDispositionFormData("attachment", fileUrl);
            //设置请求头的媒体格式类型为 application/octet-stream(二进制流数据)
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            System.out.println(fileUrl);
            return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 七牛云开放API接口: 文件删除
     *
     * @param key 删除的文件名称，在七牛云API中，对应：key
     * @return
     */
    @DeleteMapping("/delete")
    public R delete(@RequestParam("key") String key) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response response = bucketManager.delete(BUCKETNAME, key);
            int statusCode = response.statusCode;
            if (statusCode == 200) {
                return R.succeed("操作成功");
            }
            return R.fail("操作失败");
        } catch (QiniuException ex) {
            log.error(ex.response.toString());
            return R.fail(ex.response.toString());
        }
    }

    /**
     * 七牛云开放API接口：文件名称更新
     *
     * @param oldname 文件原始名称
     * @param newname 文件新名称
     * @return
     */
    @RequestMapping("/update")
    public R update(@RequestParam("oldname") String oldname, @RequestParam("newname") String newname) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.move(BUCKETNAME, oldname, BUCKETNAME, newname);
            return R.succeed("操作成功");
        } catch (QiniuException ex) {
            log.error(ex.response.toString());
            return R.fail(ex.response.toString());
        }
    }

    /**
     * 七牛云开放API接口：单个文件查询
     *
     * @param name 要查询的文件名称
     * @return
     */
    @RequestMapping("/find")
    public R find(@RequestParam("name") String name) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(BUCKETNAME, name);
            Storage storage = Storage.builder()
                    .name(name)
                    .size(fileInfo.fsize)
                    .type(fileInfo.mimeType)
                    .key(fileInfo.hash)
                    .url(URL + name)
                    .build();
            List<Storage> list = new ArrayList<>();
            list.add(storage);
            return R.succeed(list);
        } catch (QiniuException ex) {
            log.error(ex.response.toString());
            return R.fail(ex.response.toString());
        }
    }

}
