package com.autoyol.field.progress.manage.util.http;

import com.autoyol.doc.annotation.AutoDocProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

@JsonInclude(value= Include.NON_EMPTY)
public class UploadAuthVo implements Serializable{


	private static final long serialVersionUID = -5353152202498322311L;

	@AutoDocProperty(value="OSS文件服务的凭证")
	private String accessId;
	@AutoDocProperty(value="OSS文件服务的凭证")
	private String policy;
	@AutoDocProperty(value="OSS文件服务的凭证")
	private String signature;
	@AutoDocProperty(value="图片目录,例如：datePath/illegal/")
	private String dir;
	@AutoDocProperty(value="系统端自身指定的桶名称, 如: auto-test1")
	private String bucket;
	@AutoDocProperty(value="OSS文件服务的凭证, 如: http://auto-test1.oss-cn-hangzhou.aliyuncs.com")
	private String host;
	@AutoDocProperty(value="过期时间戳(10位整型)")
	private String expire;

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	@Override
	public String toString() {
		return "UploadVoucherVo{" +
				"accessId='" + accessId + '\'' +
				", policy='" + policy + '\'' +
				", signature='" + signature + '\'' +
				", dir='" + dir + '\'' +
				", bucket='" + bucket + '\'' +
				", host='" + host + '\'' +
				", expire='" + expire + '\'' +
				'}';
	}
}
