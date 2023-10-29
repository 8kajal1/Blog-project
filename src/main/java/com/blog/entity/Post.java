package com.blog.entity;

import lombok.*;

import javax.persistence.*;


import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name= "posts",
      uniqueConstraints = @UniqueConstraint(columnNames = {"title"})
)
public class Post {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="title" ,nullable = false)
    private String title;

    @Column(name="description",nullable = false)
   private String description;

    @Column(name="content",nullable = false)
   private String content;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comments;
    //private Set<Comment> comments=new HashSet<>() ;



}
