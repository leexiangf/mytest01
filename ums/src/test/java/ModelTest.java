import com.lxf.ums.UMSApplication;
import com.lxf.ums.model.adapter.BusinessAdapter;
import com.lxf.ums.model.strategy.Basketball;
import com.lxf.ums.model.strategy.Running;
import com.lxf.ums.model.strategy.SportsContext;
import com.lxf.ums.model.strategy.Swim;
import com.lxf.ums.model.template.CSGO;
import com.lxf.ums.model.template.LOL;
import com.lxf.ums.model.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname ModelTest
 * @Description
 * @Date 2022/8/10 16:07
 * @Author lxf
 */
@SpringBootTest(classes = UMSApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ModelTest {

    @Test
    public void testStrategy()  {
        SportsContext sportsContext = new SportsContext();
        SportsContext.setSports(new Basketball());
        SportsContext.sports.sportTime();
        SportsContext.setSports(new Swim());
        SportsContext.sports.sportTime();
        SportsContext.setSports(new Running());
        SportsContext.sports.sportTime();

        sportsContext.setSport(new Basketball());
        sportsContext.sportTime();
    }

    @Test
    public void testTemp(){
        Template template = new CSGO();
        template.doit();
        System.out.println("---------------");
        template=new LOL();
        template.doit();
    }

    @Test
    public void adapterTest(){
        final BusinessAdapter adapter = new BusinessAdapter();
        adapter.newSend();
    }

}
