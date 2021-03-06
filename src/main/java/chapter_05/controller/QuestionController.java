package chapter_05.controller;

import chapter_05.domain.BooleanQuestion;
import chapter_05.domain.PercentileQuestion;
import chapter_05.domain.Persistable;
import chapter_05.domain.Question;

import javax.persistence.*;
import java.time.Clock;
import java.util.List;
import java.util.function.Consumer;

public class QuestionController {
	private Clock clock = Clock.systemUTC();
	private EntityManager em;

	private static EntityManagerFactory getEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("postgres-ds");
	}

	private EntityManager em() {
		return getEntityManagerFactory().createEntityManager();
	}

	public Question find(Integer id) {
		return em().find(Question.class, id);
	}

	public List<Question> getAll() {
		return em().createQuery("select q from Question q", Question.class).getResultList();
	}

	public List<Question> findWithMatchingText(String text) {
		return em()
				.createQuery(
						"select q from Question q where q.text like '%" + text + "%'", Question.class)
				.getResultList();
	}

	// 3
	private void executeInTransaction(Consumer<EntityManager> func) {
		EntityManager em = em();

		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			func.accept(em);
			transaction.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			transaction.rollback();
		} finally {
			em.close();
		}
	}

	// 2
	private int persist(Persistable object) {
		object.setCreateTimestamp(clock.instant());
		executeInTransaction((em) -> {
			System.out.println("before em : " + em);
			em.persist(object);
			System.out.println("after em : " + em);
		});
		return object.getId();
	}

	public int addPercentileQuestion(String text, String[] answerChoices) {
		return persist(new PercentileQuestion(text, answerChoices));
	}

	void deleteAll() {
		executeInTransaction(
				(em) -> em.createNativeQuery("DELETE FROM Question").executeUpdate());
	}

	void setClock(Clock clock) {
		this.clock = clock;
	}

	// 1
	public int addBooleanQuestion(String text) {
		return persist(new BooleanQuestion(text));
	}

}
