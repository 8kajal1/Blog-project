package com.blog.service.impl;

import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.PostDto;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;


    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper=modelMapper;

    }

    //dto object convert into entity
    @Override
    public PostDto createPost(PostDto PostDto) {
        Post post = new Post();
        post.setTitle(PostDto.getTitle());
        post.setDescription(PostDto.getDescription());
        post.setContent(PostDto.getContent());
        Post newPost = postRepository.save(post);


//entity object convert to dto
        PostDto dto = new PostDto();
        dto.setId(newPost.getId());
        dto.setTitle(newPost.getTitle());
        dto.setContent(newPost.getContent());
        dto.setDescription(newPost.getDescription());

        return dto;

    }

    @Override
    public List<PostDto> ListAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

       //Sort sort= Sort.by(sortBy);

         Pageable pageable= PageRequest.of(pageNo,pageSize,sort);
        Page<Post> ListOfPosts = postRepository.findAll(pageable);
        List<Post> Posts = ListOfPosts.getContent();
        List<PostDto> PostDtos = Posts.stream().map(Post -> mapToDto(Post)).collect(Collectors.toList());
        return PostDtos;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id: " + id)
        );

        return mapToDto(post);

    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id: " + id)
        );

        com.blog.entity.Post newPost =mapToEntity(postDto);
       newPost.setId(id);

        com.blog.entity.Post updatedPost =postRepository.save(newPost);

        PostDto dto=mapToDto(updatedPost);
        return dto;
    }

    @Override
    public void deletePostById(long id) {
       Post post= postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("post not found with id:"+id)
        );
        postRepository.deleteById(id);
    }


    PostDto mapToDto(Post post) {
        PostDto dto = modelMapper.map(post, PostDto.class);

//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setContent(post.getContent());
//        dto.setDescription(post.getDescription());
       return dto;

    }

    Post mapToEntity(PostDto postDto) {
        Post post = modelMapper.map(postDto, Post.class);

//        Post post = new Post();
//        post.setId(post.getId());
//        post.setTitle(postDto.getTitle());
//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
        return post;


    }
}