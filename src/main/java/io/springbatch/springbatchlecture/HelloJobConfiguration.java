package io.springbatch.springbatchlecture;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//하나의 배치 Job을 정의하고 빈 설정

/**
 * Job를 정의한다는 것은 아래 내용을 구성한다는 것이다.
 */
@Configuration
//Job 구동 -> Step을 실행 -> Tasklet을 실행
public class HelloJobConfiguration {

    //Job을 생성하는 빌더 팩토리
    private final JobBuilderFactory jobBuilderFactory;
    //Step을 생성하는 빌더 팩토리
    private final StepBuilderFactory stepBuilderFactory;

    public HelloJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }


    @Bean
    //helloJob 이름으로 Job 생성
    public Job helloJob(){
        //.get() 안에는 Job 이름을 명시하는 것이다. 그리고 job 객체가 생성이 된다. start()에서는 Step 객체를 생성한다.
        //즉 Job이 Step을 속성으로 가지고 있는다. 자신이 가지고 있는 step을 실행을 시킨다. 그러면 Step이
        //아래에서 tasklet이라는 속성을 가지고 있는데 이것을 실행시키는 것이다.
        return jobBuilderFactory.get("helloJob")
                .start(helloStep())
                .build();
    }

    @Bean
    //helloStep 이름으로 Step 생성
    public Step helloStep(){
        //Step 안에서 단일 태스크로 수행되는 로직 구현
        /**
         * Step 안에서 실행이 될 때 이 tasklet을 호출해 준다. 그렇게 되면
         * tasklet 안에 있는 비즈니스 로직이 실행되는 것이다.
         */
        return stepBuilderFactory.get("helloStep")
                .tasklet((contribution, chunkContext)->{
                    System.out.println("Hello Spring Batch");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
