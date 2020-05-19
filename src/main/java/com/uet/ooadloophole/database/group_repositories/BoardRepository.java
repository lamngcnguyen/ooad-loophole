package com.uet.ooadloophole.database.group_repositories;

import com.uet.ooadloophole.model.business.group_elements.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BoardRepository extends MongoRepository<Board, String> {
    Board findBy_id(String _id);

    List<Board> findByGroupId(String groupId);

    List<Board> findByGroupIdAndIterationId(String groupId, String iterationId);
}
