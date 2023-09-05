package org.bmc.configuration;

import org.bmc.model.PassengerInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath:${input.file.path}")
    private Resource inputFile;

    @Qualifier("ds")
    @Autowired
    private DataSource dataSource;

    @Bean
    public ItemProcessor<PassengerInfo, PassengerInfo> processor() {
        return new PassengerInfoLogProcessor();
    }

    @Bean
    public FlatFileItemReader<PassengerInfo> reader() {
        FlatFileItemReader<PassengerInfo> itemReader = new FlatFileItemReader<>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        itemReader.setResource(inputFile);
        return itemReader;
    }

    @Bean
    public LineMapper<PassengerInfo> lineMapper() {
        DefaultLineMapper<PassengerInfo> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("PassengerId", "Survived", "Pclass", "Name", "Sex", "Age", "SibSp", "Parch", "Ticket", "Fare", "Cabin", "Embarked");
        lineTokenizer.setIncludedFields(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        BeanWrapperFieldSetMapper<PassengerInfo> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PassengerInfo.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public JdbcBatchItemWriter<PassengerInfo> itemWriter() {
        return new JdbcBatchItemWriterBuilder<PassengerInfo>()
                .dataSource(dataSource)
                .sql("INSERT INTO passenger_info (passengerId, survived, pClass, name, sex, age, sibSb, parch, ticket, fare, cabin, embarked) VALUES (:passengerId, :survived, :pClass, :name, :sex, :age, :sibSb, :parch, :ticket, :fare, :cabin, :embarked)")
                .beanMapped()
                .build();
    }

    @Bean
    public Step step(FlatFileItemReader<PassengerInfo> itemReader, JdbcBatchItemWriter<PassengerInfo> itemWriter) {
        return stepBuilderFactory.get("step")
                .<PassengerInfo, PassengerInfo>chunk(50)
                .reader(itemReader)
                .processor(processor())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job readCSVFileAndSaveInDbJob(@Qualifier("step") Step step) {
        return jobBuilderFactory.get("readCSVFileAndSaveInDbJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    // read titanic.csv, but not to save to db:
    @Bean
    public ItemWriter<PassengerInfo> csvWriter() {
        return new CsvWriter();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, FlatFileItemReader<PassengerInfo> reader) {
        return stepBuilderFactory.get("step1")
                .<PassengerInfo, PassengerInfo>chunk(50)
                .reader(reader)
                .writer(csvWriter())
                .build();
    }

    @Bean
    public Job readCSVFileJob(JobBuilderFactory jobBuilderFactory, @Qualifier("step1") Step step) {
        return jobBuilderFactory.get("readCSVFileJob")
                .start(step)
                .build();
    }
}
