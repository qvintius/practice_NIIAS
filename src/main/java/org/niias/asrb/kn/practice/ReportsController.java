package org.niias.asrb.kn.practice;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
public class ReportsController {
    @Inject
    BlanksDAO bvDAO;

    @GetMapping("/blanksviews")
    public ResponseEntity<List<BlanksViewsDTO>> getResult(){
        return ResponseEntity.ok(bvDAO.getDTOFromSQL());
    }
    @GetMapping("/blanksusers")
    public ResponseEntity<List<UsersBlanksDTO>> getReport(){
        return ResponseEntity.ok(bvDAO.getReport());
    }
}
