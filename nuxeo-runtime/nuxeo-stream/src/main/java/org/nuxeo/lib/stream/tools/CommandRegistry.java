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
package org.nuxeo.lib.stream.tools;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.lib.stream.tools.command.CatCommand;
import org.nuxeo.lib.stream.tools.command.Command;
import org.nuxeo.lib.stream.tools.command.CopyCommand;
import org.nuxeo.lib.stream.tools.command.HelpCommand;
import org.nuxeo.lib.stream.tools.command.LagCommand;
import org.nuxeo.lib.stream.tools.command.ResetCommand;
import org.nuxeo.lib.stream.tools.command.TailCommand;

/**
 * @since 9.3
 */
public class CommandRegistry {

    public List<Command> commands() {
        List<Command> ret = new ArrayList<>();
        ret.add(new HelpCommand());
        ret.add(new CatCommand());
        ret.add(new TailCommand());
        ret.add(new LagCommand());
        ret.add(new ResetCommand());
        ret.add(new CopyCommand());

        return ret;
    }
}
