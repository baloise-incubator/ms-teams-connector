/*
 * Copyright 2018 - 2021 Baloise Group
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
 */
package com.baloise.open.ms.teams.templates;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MessageCardTest {

  @Test
  void verifyBasicTemplate() {
    // read expected json file from resource
    try (final InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream("message_card_template.json");
         final InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
         final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

      StringBuilder expected = new StringBuilder();
      for (String line; (line = bufferedReader.readLine()) != null; ) {
        expected.append(line).append(System.lineSeparator());
      }

      // build the output programmatically
      final MessageCard messageCard = new MessageCard(null, "Larry Bryant created a new task");
      final Section section = Section.builder()
                                  .activityTitle("Larry Bryant created a new task")
                                  .activitySubtitle("On Project Tango")
                                  .activityImage("https://teamsnodesample.azurewebsites.net/static/img/image5.png")
                                  .markdown(true)
                                  .build();
      section.addFact(new Fact("Assigned to", "Unassigned"));
      section.addFact(new Fact("Due date", "Mon May 01 2017 17:07:18 GMT-0700 (Pacific Daylight Time)"));
      section.addFact(new Fact("Status", "Not started"));
      messageCard.addSection(section);

      final ActionCard action = new ActionCard("Add a comment");
      action.addInput(new TextInput("comment", "Add a comment here for this task", false));
      action.addAction(new Action("Add comment", "https://docs.microsoft.com/outlook/actionable-messages"));
      messageCard.addPotentialAction(action);

      final ActionCard action2 = new ActionCard("Set due date");
      action2.addInput(new DateInput("dueDate", "Enter a due date for this task"));
      action2.addAction(new Action("Save", "https://docs.microsoft.com/outlook/actionable-messages"));
      messageCard.addPotentialAction(action2);

      final ActionCard action3 = new ActionCard("Change status");
      final MultichoiceInput multichoiceInput = new MultichoiceInput("list", "Select a status", false);
      multichoiceInput.addChoice(new Choice("In Progress", "1"));
      multichoiceInput.addChoice(new Choice("Active", "2"));
      multichoiceInput.addChoice(new Choice("Closed", "3"));
      action3.addInput(multichoiceInput);
      action3.addAction(new Action("Save", "https://docs.microsoft.com/outlook/actionable-messages"));
      messageCard.addPotentialAction(action3);

      //compare the result
      assertEquals(JsonParser.parseString(expected.toString()), new Gson().toJsonTree(messageCard));

    } catch (Exception e) {
      fail("failed to read message_card_template for comparison: " + e.getMessage());
    }
  }
}