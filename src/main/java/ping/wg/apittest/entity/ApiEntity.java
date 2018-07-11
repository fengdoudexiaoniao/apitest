package ping.wg.apittest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ApiEntity {

    @Id
    @GeneratedValue
    @Column(name = "api_id")
    private Long apiId;
    @Column(name = "api_run")
    private String apirun;
    @Column(name = "api_desc")
    private String apidesc;
    @Column(name = "api_method")
    private String apimethod;
    @Column(name = "api_url")
    private String apiurl;
    @Column(name = "api_param")
    private String apiparam;
    @Column(name = "api_verify")
    private String apiverify;
    @Column(name = "api_save")
    private String apisave;

    public ApiEntity(String apirun, String apidesc, String apimethod, String apiurl, String apiparam, String apiverify, String apisave) {
        this.apirun = apirun;
        this.apidesc = apidesc;
        this.apimethod = apimethod;
        this.apiurl = apiurl;
        this.apiparam = apiparam;
        this.apiverify = apiverify;
        this.apisave = apisave;
    }

    public ApiEntity() {
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getApirun() {
        return apirun;
    }

    public void setApirun(String apirun) {
        this.apirun = apirun;
    }

    public String getApidesc() {
        return apidesc;
    }

    public void setApidesc(String apidesc) {
        this.apidesc = apidesc;
    }

    public String getApimethod() {
        return apimethod;
    }

    public void setApimethod(String apimethod) {
        this.apimethod = apimethod;
    }

    public String getApiurl() {
        return apiurl;
    }

    public void setApiurl(String apiurl) {
        this.apiurl = apiurl;
    }

    public String getApiparam() {
        return apiparam;
    }

    public void setApiparam(String apiparam) {
        this.apiparam = apiparam;
    }

    public String getApiverify() {
        return apiverify;
    }

    public void setApiverify(String apiverify) {
        this.apiverify = apiverify;
    }

    public String getApisave() {
        return apisave;
    }

    public void setApisave(String apisave) {
        this.apisave = apisave;
    }

    @Override
    public String toString() {
        return "ApiEntity{" +
                "apiId=" + apiId +
                ", apirun='" + apirun + '\'' +
                ", apidesc='" + apidesc + '\'' +
                ", apimethod='" + apimethod + '\'' +
                ", apiurl='" + apiurl + '\'' +
                ", apiparam='" + apiparam + '\'' +
                ", apiverify='" + apiverify + '\'' +
                ", apisave='" + apisave + '\'' +
                '}';
    }
}
