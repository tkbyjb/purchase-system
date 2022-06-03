import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.purchase.global.ResultState;

public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator
                .create("jdbc:mysql://localhost:3306/school_purchase?characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "12345678")
                .globalConfig(builder -> {
                    builder
//                            .author("xxx") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\code\\1_my_projects\\1-school-purchase\\purchase-parent\\purchase-api\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.purchase") // 设置父包名
                    .entity("po")
                    ;
//                            .moduleName("po"); // 设置父包模块名
//                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D://")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
                            .addExclude()
//                            .addInclude("t_simple") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .enableCapitalMode()
                            ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
