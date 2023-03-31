import com.lxf.ums.UMSApplication;
import com.lxf.ums.lock.redisson.serveice.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname LockTest
 * @Description
 * @Date 2023/3/31 16:32
 * @Author lxf
 */
@SpringBootTest(classes = UMSApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class LockTest {

    @Autowired
    private RedissonService redissonService;

    @Test
    public void testRedisson1(){
        String userId1 = "U1";
        String userId2 = "U2";
        String userId3 = "U3";

        for (int i = 0; i < 10; i++) {
            int finalI = i;
           // log.info("finalI:{} ,余{}",finalI,finalI%3);
            new Thread(() -> {
                if (finalI % 3 == 1) {
                    String join = redissonService.join(userId1);
                    log.warn(join);
                }
                if (finalI % 3 == 2) {
                    String join = redissonService.join(userId2);
                    log.warn(join);
                }
                if (finalI % 3 == 0) {
                    String join = redissonService.join(userId3);
                    log.warn(join);
                }
            }).start();
        }
    }
}
