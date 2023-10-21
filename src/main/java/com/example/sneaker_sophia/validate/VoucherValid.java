package com.example.sneaker_sophia.validate;

import com.example.sneaker_sophia.dto.VoucherDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VoucherValid implements ConstraintValidator<VoucherValidConfig, VoucherDTO>  {
    private boolean shouldCheck; // Cờ biểu thị có nên kiểm tra lỗi hay không



    @Override
    public boolean isValid(VoucherDTO s, ConstraintValidatorContext context) {
        String mes = null;
        int check = 0;


        if (s.getTen()== null || s.getTen().trim().length()==0){
            context.buildConstraintViolationWithTemplate("Vui lòng nhập tên").addPropertyNode("ten").addConstraintViolation();
            return false;
        }


        return check ==0;
    }


















}