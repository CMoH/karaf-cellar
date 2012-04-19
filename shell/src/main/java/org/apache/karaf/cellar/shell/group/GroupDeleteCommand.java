/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.cellar.shell.group;

import org.apache.karaf.cellar.core.Group;
import org.apache.karaf.cellar.core.Node;
import org.apache.karaf.cellar.core.control.ManageGroupAction;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;

import java.util.LinkedList;
import java.util.List;

@Command(scope = "cluster", name = "group-delete", description = "Delete a cluster group.")
public class GroupDeleteCommand extends GroupSupport {

    @Argument(index = 0, name = "group", description = "The cluster group name.", required = false, multiValued = false)
    String group;

    @Override
    protected Object doExecute() throws Exception {
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            Group g = groupManager.findGroupByName(group);
            if (g == null) {
                System.out.println("Group " + group + " doesn't exist.");
                return null;
            }
            List<String> nodes = new LinkedList<String>();

            if (g.getNodes() != null && !g.getNodes().isEmpty()) {
                for (Node n : g.getNodes()) {
                    nodes.add(n.getId());
                }
                doExecute(ManageGroupAction.QUIT, group, null, nodes);
            }

            groupManager.deleteGroup(group);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
        return null;
    }

}
