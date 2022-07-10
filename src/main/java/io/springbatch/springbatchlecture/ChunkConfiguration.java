package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ChunkConfiguration {

    //Job을 생성하는 빌더 팩토리
    private final JobBuilderFactory jobBuilderFactory;
    //Step을 생성하는 빌더 팩토리
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    //helloJob 이름으로 Job 생성
    public Job helloJob() {
        //.get() 안에는 Job 이름을 명시하는 것이다. 그리고 job 객체가 생성이 된다. start()에서는 Step 객체를 생성한다.
        //즉 Job이 Step을 속성으로 가지고 있는다. 자신이 가지고 있는 step을 실행을 시킨다. 그러면 Step이
        //아래에서 tasklet이라는 속성을 가지고 있는데 이것을 실행시키는 것이다.
        return jobBuilderFactory.get("helloJob")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                //타입을 input, output를 정한다
                .<String, String>chunk(2)
                //ItemReader
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3","item4", "item5")))
                //ItemProcessor
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    //하나의 item를 처리하도록 파라미터로 전달됨
                    public String process(String item) throws Exception {
                        Thread.sleep(300);
                        System.out.println("item = " + item);
                        return "my "+item;
                    }
                })
                //ItemWriter
                .writer(new ItemWriter<String>() {
                    @Override
                    //하나의 item이 아닌 items list가 전달된다. 즉 개별 처리가 아닌 일괄처리인 것이다.
                    public void write(List<? extends String> items) throws Exception {
                        Thread.sleep(300);
                        System.out.println("items  = "+items);
                    }
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext)->{
                    System.out.println("step2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
