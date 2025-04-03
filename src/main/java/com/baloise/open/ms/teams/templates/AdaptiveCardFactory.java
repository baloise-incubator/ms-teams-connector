package com.baloise.open.ms.teams.templates;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AdaptiveCardFactory {
    final AdaptiveCard instance;
    final FactSet facts;

    private AdaptiveCardFactory(String title, String message) {
        instance = createSimpleAdaptiveCard(title, message);
        facts = FactSet.builder()
                .facts(List.of())
                .build();
    }

    public static AdaptiveCard createSimpleAdaptiveCard(final String title, final String message) {
        TextBlock titleBlock = TextBlock.builder()
                .text(title)
                .weight(TextBlock.TextWeight.Bolder)
                .style(TextBlock.TextStyle.Heading)
                .build();
        TextBlock messageBlock = TextBlock.builder()
                .text(message)
                .build();
        return AdaptiveCard.builder()
                .body(List.of(titleBlock, messageBlock))
                .build();
    }

    public static AdaptiveCard createSimpleAdaptiveCard(final String title, final String message, Map<String, String> facts) {
        final AdaptiveCard adaptiveCard = createSimpleAdaptiveCard(title, message);
        FactSet factSet = FactSet.builder()
                .facts(
                        facts.entrySet().stream()
                                .map(entry -> Fact.builder()
                                        .title(entry.getKey())
                                        .value(entry.getValue())
                                        .build())
                                .toList()
                )
                .build();

        adaptiveCard.setBody(Stream.concat(adaptiveCard.getBody().stream(), Stream.of(factSet)).toList());
        return adaptiveCard;
    }

    public static AdaptiveCardFactory builder(final String title, final String message) {
        return new AdaptiveCardFactory(title, message);
    }

    public AdaptiveCardFactory withFact(final Fact fact) {
        if (fact != null) {
            facts.setFacts(Stream.concat(facts.getFacts().stream(), Stream.of(fact)).toList());
        }
        return this;
    }

    public AdaptiveCardFactory withFact(final String title, final String value) {
        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(value)) {
            return withFact(new Fact(title, value));
        }
        return this;
    }

    public AdaptiveCard build() {
        if (!facts.getFacts().isEmpty()) {
            instance.setBody(Stream.concat(instance.getBody().stream(), Stream.of(facts)).toList());
        }
        return instance;
    }
}
