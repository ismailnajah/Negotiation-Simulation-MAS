package com.example.android.distributeurdeau.models.Constents;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Templates {

    public static final MessageTemplate AUTHENTICATION = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                    MessageTemplate.MatchPerformative(ACLMessage.REFUSE)
            ),
            MessageTemplate.MatchOntology(Onthologies.authentication)
    );

    public static final MessageTemplate REGISTRATION = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
            ),
            MessageTemplate.MatchOntology(Onthologies.registration)
    );

    public static final MessageTemplate MODIFICATION = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE),
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)
            ),
            MessageTemplate.MatchOntology(Onthologies.plot_modification)
    );

    public static final MessageTemplate ADDITION = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE),
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)
            ),
            MessageTemplate.MatchOntology(Onthologies.plot_addition)
    );

    public static final MessageTemplate SEND = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE),
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)
            ),
            MessageTemplate.MatchOntology(Onthologies.plot_send)
    );

    public static final MessageTemplate DELETE = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE),
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)
            ),
            MessageTemplate.MatchOntology(Onthologies.plot_removing)
    );

    public static final MessageTemplate CULTURE_DATA = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE),
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)
            ),
            MessageTemplate.MatchOntology(Onthologies.culture_data)
    );

    public static final MessageTemplate PROPOSAL_STATUS = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
            ),
            MessageTemplate.MatchOntology(Onthologies.propose_plot)
    );

    public static final MessageTemplate CANCEL_NEGOTIATION = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
            ),
            MessageTemplate.MatchOntology(Onthologies.cancel_negotiation)
    );

    public static final MessageTemplate NOTIFICATION = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchOntology(Onthologies.NOTIFY)
    );

    public static final MessageTemplate ACCEPT = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
            ),
            MessageTemplate.MatchOntology(Onthologies.ACCEPT_PLAN)
    );

    public static final MessageTemplate REFUSE = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
            ),
            MessageTemplate.MatchOntology(Onthologies.REFUSE_PLAN)
    );

    public static final MessageTemplate DOTATION = MessageTemplate.and(
            MessageTemplate.or(
                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                    MessageTemplate.MatchPerformative(ACLMessage.FAILURE)
            ),
            MessageTemplate.MatchOntology(Onthologies.SET_DOTATION)
    );
    public static final MessageTemplate ANALYSE = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
}
