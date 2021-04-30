package com.product.product.Entity;

import com.product.product.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EnableR2dbcAuditing
@Table("mmbr")
public class Mmbr {
    public Mmbr(String mmbrId, String mmbrPwd){
    	this.mmbrId = mmbrId;
    	this.mmbrPwd = mmbrPwd;
	}

	@Id
    private String mmbrId;
	private String mmbrPwd;
	private String mmbrNm; 
	private String mmbrEmail;
	private String mmbrStusCd;
	private String rtGrpNo;
	@CreatedDate
    private LocalDateTime mmbrJoinDtm;
	private LocalDateTime mmbrWdtrDtm;
	@CreatedDate
	private LocalDateTime frstRegDtm;
	@LastModifiedBy
	private String frstRegId;
	@LastModifiedDate
	private LocalDateTime finlEditDtm;

	@Transient
	private List<Role> roles;

	public boolean getPwdChk(String mmbrPwd){
		return this.mmbrPwd.equals(mmbrPwd);
	}
}
