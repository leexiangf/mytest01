package com.lxf.ums.enums;

/**
 * @ClassName : SysRoleCode
 * @Description TODO
 * @Date 2022/4/10 19:43
 * @Created lxf
 */
public enum SysRoleCode {

    ROLE_GUEST("8000","ROLE_GUEST"),
    ROLE_ADMIN("9000","ROLE_ADMIN"),
    ROLE_USER("9001","ROLE_USER");

    private String code;

    private String roleName;

    SysRoleCode(String code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public String code(){
        return code;
    }
    public String roleName(){
        return roleName;
    }
}
