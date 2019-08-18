package me;
import java.io.File;

import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetWriter;

import org.apache.avro.Schema;
import org.apache.hadoop.fs.Path;
import org.apache.avro.generic.GenericData;

import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.avro.AvroParquetReader;

import java.io.IOException;
import java.util.List;

class ParquetCreateAndRead {

        final String fileUrl = "file:///tmp/mine.parquet";
        final Path   filePath = new Path(fileUrl);

        public void create() {
                try {
                        Schema schema = new Schema.Parser().parse(new File("mine.json"));

                        ParquetWriter<GenericRecord> writer = AvroParquetWriter
                                .<GenericRecord>builder(filePath)
                                .withSchema(schema)
                                .build();

                        GenericRecord rec = new GenericData.Record(schema);
                        rec.put("field", "It is over 9000!");
                        writer.write(rec);
                        rec.put("field", "What? 9000?");
                        writer.write(rec);
                        writer.close();
                } catch (IOException ex) {
                        System.err.println("EXCEPTION " + ex.toString());
                }
        }

        public void read() {
                try {
                        ParquetReader<GenericRecord> reader = AvroParquetReader
                                .<GenericRecord>builder(filePath)
                                .build();

                        GenericRecord rec;
                        while ((rec = reader.read()) != null) {
                                List<Schema.Field> fields = rec.getSchema().getFields();
                                for (Schema.Field field : fields) {
                                        System.out.print(String.format("\n%s = %s \n",
                                                                field.name(),
                                                                rec.get(field.pos())));
                                }
                        }
                        reader.close();
                } catch (IOException ex) {
                        System.err.println("EXCEPTION " + ex.toString());
                }

        }
}

