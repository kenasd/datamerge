package com.olek.datamerge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.olek.datamerge.service.DataMergeService;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class DataMerge {

    public static void main(String[] args) {
        if (args.length < 2) {
            log.error("Wrong List of arguments: {}", Arrays.toString(args));
            log.error("Expected: output file path(String), input file paths (String ...)");
            throw new RuntimeException("Wrong List of arguments");
        }

        String output = args[0];
        String[] inputs = new String[args.length - 1];
        System.arraycopy(args, 1, inputs, 0, args.length - 1);

        DataMergeService dataMergeService = new DataMergeService(new ObjectMapper(), new XmlMapper());
        dataMergeService.readFiles(output, inputs);
    }
}
