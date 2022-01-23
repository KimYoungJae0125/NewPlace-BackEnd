package shop.newplace.Bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Bank {

    Hankook("한국은행", "001");
//    산업은행("002"),
//    기업은행("003"),
//    국민은행("004"),
//    외환은행("005"),
//    수협중앙회("006"),
//    수출입은행("007"),
//    농협중앙회("008"),
//    단위_농축협("012"),
//    우리은행("020"),
//    SC은행("023"),
//    한국씨티은행("027"),
//    새마을금고중앙회("045"),
//    신협중앙회("048"),
//    우체국("071"),
//    하나은행("081"),
//    신한은행("088");

    private String bankName;
    private String bankCode;

}
