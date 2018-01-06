package com.lenglish.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.lenglish.domain.Exam;
import com.lenglish.domain.Lesson;
import com.lenglish.service.QuestionService;
import com.lenglish.service.dto.QuestionDTO;
import com.lenglish.service.util.DateTimeUtil;
import com.lenglish.web.rest.errors.BadRequestAlertException;
import com.lenglish.web.rest.util.HeaderUtil;
import com.lenglish.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Question.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

	private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

	private static final String ENTITY_NAME = "question";

	private final QuestionService questionService;

	public QuestionResource(QuestionService questionService) {
		this.questionService = questionService;
	}

	/**
	 * POST /questions : Create a new question.
	 *
	 * @param questionDTO
	 *            the questionDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         questionDTO, or with status 400 (Bad Request) if the question has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/questions")
	@Timed
	public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO)
			throws URISyntaxException {
		log.debug("REST request to save Question : {}", questionDTO);
		if (questionDTO.getId() != null) {
			throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
		}
		questionDTO.setCreateDate(DateTimeUtil.now());
		QuestionDTO result = questionService.save(questionDTO);
		return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /questions : Updates an existing question.
	 *
	 * @param questionDTO
	 *            the questionDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         questionDTO, or with status 400 (Bad Request) if the questionDTO is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         questionDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/questions")
	@Timed
	public ResponseEntity<QuestionDTO> updateQuestion(@Valid @RequestBody QuestionDTO questionDTO)
			throws URISyntaxException {
		log.debug("REST request to update Question : {}", questionDTO);
		if (questionDTO.getId() == null) {
			return createQuestion(questionDTO);
		}
		QuestionDTO result = questionService.save(questionDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionDTO.getId().toString())).body(result);
	}

	/**
	 * GET /questions : get all the questions.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of questions in
	 *         body
	 */
	@GetMapping("/questions")
	@Timed
	public ResponseEntity<List<QuestionDTO>> getAllQuestions(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Questions");
		Page<QuestionDTO> page = questionService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /questions/:id : get the "id" question.
	 *
	 * @param id
	 *            the id of the questionDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         questionDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/questions/{id}")
	@Timed
	public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
		log.debug("REST request to get Question : {}", id);
		QuestionDTO questionDTO = questionService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionDTO));
	}

	/**
	 * DELETE /questions/:id : delete the "id" question.
	 *
	 * @param id
	 *            the id of the questionDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/questions/{id}")
	@Timed
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
		log.debug("REST request to delete Question : {}", id);
		questionService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	// new code

	@GetMapping("/questions_by_lesson/{id}")
	@Timed
	public ResponseEntity<List<QuestionDTO>> getAllQuestionsByLesson(@ApiParam Pageable pageable,
			@PathVariable Long id) {
		log.debug("REST request to get a page of Questions by Lesson id: {}", id);
		Lesson lesson = new Lesson();
		lesson.setId(id);
		Page<QuestionDTO> page = questionService.findAllByLesson(pageable, lesson);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions_by_lesson/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/questions_by_exam/{id}")
	@Timed
	public ResponseEntity<List<QuestionDTO>> getAllQuestionsByExam(@ApiParam Pageable pageable,
			@PathVariable Long id) {
		log.debug("REST request to get a page of Questions by Lesson id: {}", id);
		Exam exam = new Exam();
		exam.setId(id);
		Page<QuestionDTO> page = questionService.findAllByExam(pageable, exam);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions_by_exam/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}
