package com.example.demo.dao.Student;

import com.example.demo.entity.Student;
import com.example.demo.vo.StudentVo;
import org.apache.ibatis.annotations.Param;

/**
 * Created by T011689 on 2018/10/24.
 */
public interface StudentDao {
    StudentVo selectByName(@Param("studentName") String studentName);
    Integer register(Student student);
    Integer insert(Student student);
}
