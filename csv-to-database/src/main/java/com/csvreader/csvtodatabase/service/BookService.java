package com.csvreader.csvtodatabase.service;

import com.csvreader.csvtodatabase.encryptor.PgpEncryptor;
import com.csvreader.csvtodatabase.model.BookModel;
import com.csvreader.csvtodatabase.repository.BookRepository;
import com.dropbox.core.v2.DbxClientV2;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final DbxClientV2 dbxClientV2;
    private final BookRepository bookRepository;
    private final PgpEncryptor encryptor;

    // path of csv file
    @Value("${csv.file-path}")
    private String FILEPATH;


    //    To Encrypt Stream Data received From mongodb
    public void getFileEncrypted() throws Exception {

        var csvMapper = new CsvMapper();

        // Set CsvSchema To withoutQuote Character
        var columns = csvMapper.schemaFor(BookModel.class).withUseHeader(true).withoutQuoteChar();
        var bookList = bookRepository.findAll();
        var byteArrayInputStream = new ByteArrayInputStream(csvMapper.writer(columns).writeValueAsBytes(bookList));

        String s = new String(byteArrayInputStream.readAllBytes());
        System.out.println(s);

        // Getting Values of byteArrayInputStream in byte Array
        var bytesToEncrypt = byteArrayInputStream.readAllBytes();

//        var encryptedByteArrayInputStream = encryptor.encryption(bytesToEncrypt);



//        InputStream encryptedInputStream = encryptedByteArrayInputStream;

        String filePath1 = "/Apps/csv-encrypted-file/data.csv";

        String dataOneCsv = "/Apps/csv-encrypted-file/DataThree.csv";


        String dataMongo = "/Apps/csv-encrypted-file/mongoDataThree.csv";



        var inputStream = dbxClientV2.files().download(filePath1).getInputStream();


        String s1 = new String(inputStream.readAllBytes());
        System.out.println(s1);
        System.out.println();

        String s2 = new String(inputStream.readAllBytes());
        System.out.println(s2);


//        dbxClientV2.files().upload(dataOneCsv).uploadAndFinish(inputStream);

//        dbxClientV2.files().upload(dataMongo).uploadAndFinish(byteArrayInputStream);



        System.out.println("File Encrypted successfully");
    }

    // To Store CSV data to mongodb
    public void csvToByteArrayConverter() {

        var bookModelSchema = CsvSchema.emptySchema().withHeader();

        var csvMapper = new CsvMapper();
        var objectReader = csvMapper.readerFor(BookModel.class).with(bookModelSchema);

        try (var fileReader = new FileReader(FILEPATH)) {
            MappingIterator<BookModel> iterator = objectReader.readValues(fileReader);
            var bookModels = iterator.readAll();
            bookRepository.saveAll(bookModels);

        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("|| Unable to process the CSV file ||");
        }

    }

}
