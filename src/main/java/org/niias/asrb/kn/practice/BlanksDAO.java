package org.niias.asrb.kn.practice;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.niias.asrb.kn.repository.UserRepository;
import org.niias.asrb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import static org.niias.asrb.kn.model.QBlank.blank;
import static org.niias.asrb.kn.model.QBlankViewMark.blankViewMark;

@Component
public class BlanksDAO {
    private final JPAQueryFactory qf;
    private final UserRepository uRepo;

    private final ModelMapper modelMapper;
    @Autowired
    public BlanksDAO(JPAQueryFactory qf, UserRepository uRepo, ModelMapper modelMapper) {
        this.qf = qf;
        this.uRepo = uRepo;
        this.modelMapper = modelMapper;
    }


    public List<BlanksViewsDTO> getDTOFromSQL(){
        StringTemplate arr1 = Expressions.stringTemplate("cast(array_agg({0}) as text)", blank.id);
        List<Tuple> res1 = qf.from(blank)
                .select(blank.created.date.year(), blank.created.date.month(), blank.created.id, blank.id.countDistinct(), arr1)
                .groupBy(blank.created.date.year(), blank.created.date.month(), blank.created.id)
                .orderBy(blank.created.id.asc()).fetch();

        StringTemplate arr2 = Expressions.stringTemplate("cast(array_agg({0}) as text)", blankViewMark.id);
        List<Tuple> res2=qf.from(blankViewMark)
                .select(blankViewMark.viewDate.year(), blankViewMark.viewDate.month(), blankViewMark.userId, blankViewMark.id.countDistinct(), arr2)
                .groupBy(blankViewMark.viewDate.year(), blankViewMark.viewDate.month(), blankViewMark.userId)
                .orderBy(blankViewMark.userId.asc()).fetch();

        List<BlanksViewsDTO> dtoList = new SQLConverter().getAll(res1, res2);
        return dtoList;
    }
    public List<UsersBlanksDTO> getReport(){
        List<UsersBlanksDTO> ubDTO = new ArrayList<>();

        for (BlanksViewsDTO bvDTO:getDTOFromSQL()) {
            User user = uRepo.findById(bvDTO.getCreatedUserId()).orElse(null);
            UsersBlanksDTO bUserDTO = new UsersBlanksDTO();
            if (user != null)
                bUserDTO = enreachDTO(bvDTO, user);
            else continue;
            ubDTO.add(bUserDTO);
        }
        return ubDTO;
    }
    private UsersBlanksDTO enreachDTO(BlanksViewsDTO bvDTO, User user){
        UsersBlanksDTO buDTO = modelMapper.map(bvDTO, UsersBlanksDTO.class);
        buDTO.setRailwayShortName(user.getRailroad().getShortName());
        buDTO.setFio(user.getFio());
        buDTO.setSubdivision(user.getSubdivision());

        return buDTO;
    }
}