import com.lxf.common.entity.SysNickName;
import com.lxf.ums.UMSApplication;
import com.lxf.ums.mapper.SysNickNameMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName : TransactionalTest
 * @Description  事务测试
 * @Date 2022/4/27 22:07
 * @Created lxf
 */
@SpringBootTest(classes = UMSApplication.class)
@Slf4j
public class TransactionalTest {


    @Autowired
    private SysNickNameMapper sysNickNameMapper;

    @Test
    @Transactional()
    public void test() {
        insertOne();
        insertTwo();
    }

    private boolean insertOne(){
        SysNickName nickName = new SysNickName();
        nickName.setNickName("张三丰");
        sysNickNameMapper.insert(nickName);
        return true;
    }

    private boolean insertTwo(){
        SysNickName nickName = new SysNickName();
        nickName.setNickName("王重阳");
        System.out.println(1/0);
        sysNickNameMapper.insert(nickName);
        return true;
    }
}
