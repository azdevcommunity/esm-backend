package com.example.medrese.Controller;


import com.example.medrese.DTO.Request.Update.UpdateVideo;
import com.example.medrese.DTO.Response.VideoResponse;
import com.example.medrese.Service.VideoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    @GetMapping
    public ResponseEntity<?> getAll() {
       return ResponseEntity.ok(videoService.getAll());
    }


    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> getAll(@RequestParam int page,
                                    @RequestParam(name = "size")
                                    @Min(value = 1, message = "Size must be at least 1")
                                    @Max(value = 40, message = "Size cannot be greater than 40")  int size,
                                    @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(videoService.getAllPaging(page, size, search));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<VideoResponse> getById(@PathVariable String id) {
//        return videoService.getById(id);
//    }



    @GetMapping(params = "playlistId")
    public ResponseEntity<List<VideoResponse>> getByPlaylistId(@RequestParam("playlistId") String playlistId) {
        return ResponseEntity.ok(videoService.getByPlaylistId(playlistId));
    }

//    @PostMapping
//    public ResponseEntity<CreateVideo> add(@Valid @RequestBody CreateVideo Video) {
//        return videoService.add(Video);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateVideo> update(@Valid @RequestBody UpdateVideo video, @PathVariable String id) {
        return videoService.update(video, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VideoResponse> delete(@PathVariable String id) {
        return videoService.delete(id);
    }

    @PutMapping
    public void updateVideos() {
//        videoService.addOrUpdateVideos();
    }

    @GetMapping(params = {"sortBy", "sortOrder"})
    public ResponseEntity<List<VideoResponse>> getAllSorted(@RequestParam(name = "sortBy") String fieldName, @RequestParam(name = "sortOrder") String sortOrder) {
        return videoService.getSortedVideosBySpecificField(fieldName, sortOrder);
    }

    @GetMapping(params = {"playlistId", "sortBy", "sortOrder"})
    public ResponseEntity<List<VideoResponse>> getPlayListVideosSorted(@RequestParam(name = "sortBy") String fieldName, @RequestParam String sortOrder, @RequestParam String playlistId) {
        return videoService.getSortedPlayListVideosBySpecificField(fieldName, playlistId, sortOrder);
    }

    @GetMapping(params = {"playlistId", "page", "maxResult"})
    public ResponseEntity<?> getAllPaged(@RequestParam String playlistId,@RequestParam int page,@RequestParam(name = "maxResult") int size) {
        return videoService.getByPlaylistIdAndPagination(playlistId, page, size);
    }
}
