/*
 * (C) Copyright 2017 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     bdelbosc
 */
package org.nuxeo.lib.stream.tools.command;

import java.time.Duration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.nuxeo.lib.stream.computation.Record;
import org.nuxeo.lib.stream.log.LogManager;
import org.nuxeo.lib.stream.log.LogRecord;
import org.nuxeo.lib.stream.log.LogTailer;
import org.nuxeo.lib.stream.tools.renderer.Renderer;

/**
 * @since 9.3
 */
public class CatCommand extends Command {

    protected static final String NAME = "cat";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void updateOptions(Options options) {
        options.addOption(
                Option.builder("n").longOpt("lines").desc("Render the first N records").hasArg().argName("N").build());
        options.addOption(Option.builder("l")
                                .longOpt("log-name")
                                .desc("Log name")
                                .required()
                                .hasArg()
                                .argName("LOG_NAME")
                                .build());
        options.addOption(
                Option.builder("g").longOpt("group").desc("Consumer group").hasArg().argName("GROUP").build());
        options.addOption(
                Option.builder().longOpt("render").desc("Output rendering").hasArg().argName("FORMAT").build());

    }

    @Override
    public boolean run(LogManager manager, CommandLine cmd) throws InterruptedException {
        int limit = Integer.parseInt(cmd.getOptionValue("lines", "-1"));
        String name = cmd.getOptionValue("log-name");
        String render = cmd.getOptionValue("render", "default");
        String group = cmd.getOptionValue("group", "tools");
        cat(manager, name, group, limit, getRecordRenderer(render));
        return true;
    }

    protected void cat(LogManager manager, String name, String group, int limit, Renderer render)
            throws InterruptedException {
        render.header();
        try (LogTailer<Record> tailer = manager.createTailer(group, name)) {
            int count = 0;
            do {
                LogRecord<Record> record = tailer.read(Duration.ofMillis(1000));
                if (record == null) {
                    break;
                }
                count++;
                render.accept(record);
            } while (limit < 0 || (count < limit));
        }
        render.footer();
    }
}
