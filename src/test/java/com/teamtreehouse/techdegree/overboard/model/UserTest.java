package com.teamtreehouse.techdegree.overboard.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UserTest {
  private Board board;
  private User questioner;
  private User answerer;
  private Question question;
  private Answer answer;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    board = new Board("Java");
    questioner = board.createUser("Safet");
    answerer = board.createUser("Leila");

    question = questioner.askQuestion("What's up? ");
    answer = answerer.answerQuestion(question, "Nothing.");
  }

  @Test
  public void questionerReputationGoesUpByFiveIfUpVoted() throws Exception {

    answerer.upVote(question);

    assertEquals( 5, questioner.getReputation());
  }

  @Test
  public void answererReputationGoesUpByTenIfAnswerIsUpVoted() throws Exception {

    questioner.upVote(answer);

    assertEquals(10, answerer.getReputation());
  }

  @Test
  public void answerAcceptedGivesAnswererFifteenPoints() throws Exception {

    questioner.acceptAnswer(answer);

    assertEquals(15, answerer.getReputation());
  }

  @Test
  public void authorCannotUpVoteOwnQuestion() throws Exception {

    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself!");

    questioner.upVote(question);
  }

  @Test
  public void authorCannotDownVoteOwnQuestion() throws Exception {

    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself!");

    questioner.downVote(question);
  }

  @Test
  public void authorCannotUpVoteOwnAnswer() throws Exception {

    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself!");

    answerer.upVote(answer);
  }

  @Test
  public void authorCannotDownVoteOwnAnswer() throws Exception {

    thrown.expect(VotingException.class);
    thrown.expectMessage("You cannot vote for yourself!");

    answerer.downVote(answer);
  }

  @Test
  public void questionerCanAcceptAnswer() throws Exception {
    questioner.acceptAnswer(answer);

    assertTrue(answer.isAccepted());
  }

  @Test
  public void noOtherUserCanAcceptTheAnswerExceptTheQuestioner() throws Exception {
    String message = String.format("Only %s can accept this answer as it is their question",
                                    questioner.getName());
    thrown.expect(AnswerAcceptanceException.class);
    thrown.expectMessage(message);

    answerer.acceptAnswer(answer);
  }

  @Test
  public void questionerReputationDecreasesIfQuestionIsDownVoted() throws Exception {
    answerer.downVote(question);

    assertEquals(-1, questioner.getReputation());
  }

  @Test
  public void answererReputationDecreasesIfAnswerIsDownVoted() throws Exception {
    questioner.downVote(answer);

    assertEquals(-1, answerer.getReputation());
  }
}