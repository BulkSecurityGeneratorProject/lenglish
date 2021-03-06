package com.lenglish.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lenglish.domain.Exam;
import com.lenglish.domain.Lesson;
import com.lenglish.domain.Question;
import com.lenglish.repository.QuestionRepository;
import com.lenglish.service.dto.QuestionDTO;
import com.lenglish.service.mapper.QuestionMapper;

/**
 * Service Implementation for managing Question.
 */
@Service
@Transactional
public class QuestionService {

	private final Logger log = LoggerFactory.getLogger(QuestionService.class);

	private final QuestionRepository questionRepository;

	private final QuestionMapper questionMapper;

	public QuestionService(QuestionRepository questionRepository, QuestionMapper questionMapper) {
		this.questionRepository = questionRepository;
		this.questionMapper = questionMapper;
	}

	/**
	 * Save a question.
	 *
	 * @param questionDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public QuestionDTO save(QuestionDTO questionDTO) {
		log.debug("Request to save Question : {}", questionDTO);
		Question question = questionMapper.toEntity(questionDTO);
		question = questionRepository.save(question);
		return questionMapper.toDto(question);
	}

	/**
	 * Get all the questions.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<QuestionDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Questions");
		return questionRepository.findAll(pageable).map(questionMapper::toDto);
	}

	/**
	 * Get one question by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public QuestionDTO findOne(Long id) {
		log.debug("Request to get Question : {}", id);
		Question question = questionRepository.findOneWithEagerRelationships(id);
		return questionMapper.toDto(question);
	}

	/**
	 * Delete the question by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Question : {}", id);
		questionRepository.delete(id);
	}

	public Page<QuestionDTO> findAllByLesson(Pageable pageable, Lesson lesson) {
		log.debug("Request to get all Questions");
		return questionRepository.findAllByLesson(pageable, lesson).map(questionMapper::toDto);
	}

	public Page<QuestionDTO> findAllByExam(Pageable pageable, Exam exam) {
		log.debug("Request to get all Questions");
		return questionRepository.findAllByExams(pageable, exam).map(questionMapper::toDto);
	}

}
