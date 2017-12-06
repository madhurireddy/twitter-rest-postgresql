package com.twitter.sample.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tweet JPA entity class.  
 * @author mdevidi
 */
@Entity
@Table(name = "TWEET")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Tweet.findAll", query = "SELECT d FROM Tweet d"),
	@NamedQuery(name = "Tweet.findByTweetId", query = "SELECT d FROM Tweet d WHERE d.tweetId = :tweetId"),
	@NamedQuery(name = "Tweet.findByEmployeeId", query = "SELECT d FROM Tweet d WHERE d.employeeId = :employeeId"),
	@NamedQuery(name = "Tweet.findByEmployeeIds", query = "SELECT d FROM Tweet d WHERE d.employeeId in (:employeeIds)"),
})
public class Tweet implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long tweetId;

	@Column(name = "employee_id")
	private Integer employeeId;

	@Column(name = "tweet_content")
	private String tweetContent;
	
	
    @Column(name = "tweet_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tweetDate;


	public Tweet() {
	}

	public Tweet(Long tweetId) {
		this.tweetId = tweetId;
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	
	public Date getTweetDate() {
        return tweetDate;
    }

    public void setTweetDate(Date tweetDate) {
        this.tweetDate = tweetDate;
    }

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (tweetId != null ? tweetId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Tweet)) {
			return false;
		}
		Tweet other = (Tweet) object;
		if ((this.tweetId == null && other.tweetId != null) || (this.tweetId != null && !this.tweetId.equals(other.tweetId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.twitter.sample.domain.Tweet[ tweetId=" + tweetId + " ]";
	}

	public String getTweetContent() {
		return tweetContent;
	}

	public void setTweetContent(String tweetContent) {
		this.tweetContent = tweetContent;
	}

}
