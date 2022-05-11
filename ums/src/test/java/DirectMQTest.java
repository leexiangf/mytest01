import com.lxf.ums.UMSApplication;
import com.lxf.ums.mq.direct.DirectProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName : DirectMQTest
 * @Description
 * @Date 2022/4/19 21:53
 * @Created lxf
 */

@SpringBootTest(classes = UMSApplication.class)
@Slf4j
public class DirectMQTest {

    @Autowired
    private DirectProducer directProducer;

    @Test
    public void test() throws InterruptedException {
        directProducer.sendMessageWithProperties("Hello,2021");
        Thread.sleep(10000);
    }
}
