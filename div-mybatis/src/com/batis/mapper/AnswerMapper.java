package com.batis.mapper;

import java.util.List;

import com.batis.po.Answer;

public interface AnswerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Answer record);

    List<Answer> selectAll();
    
    List<Answer> selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Answer record);

}