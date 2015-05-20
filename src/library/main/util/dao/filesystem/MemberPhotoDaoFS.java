package library.main.util.dao.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import javafx.scene.image.Image;
import library.main.model.Member;

/**
 * @author morrisseymarr
 *
 */
public class MemberPhotoDaoFS {
	public static final String NO_PHOTO = "library/main/resources/images/no_photo.png";
	public static final Path MEMBER_PHOTO_PATH = Paths
			.get("../resources/images");
	public static final Image DEFAULT_IMAGE = new Image(
			ClassLoader
					.getSystemResourceAsStream("library/main/resources/images/your_face_here.png"));
	private Member member;

	public MemberPhotoDaoFS(Member member) {
		this.member = member;
	}

	public MemberPhotoDaoFS() {
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String insertPhotoMember(Path sourcePhotoPath) throws SQLException,
			IOException {
		String photoMemberActualPath = null;
		if (sourcePhotoPath != null) {
			photoMemberActualPath = MEMBER_PHOTO_PATH.resolve(
					Long.toString(this.member.getId())).toString();
			if (Files.exists(Paths.get(photoMemberActualPath))) {
				Files.delete(Paths.get(photoMemberActualPath));
			}
			member.setPhoto(photoMemberActualPath);
			Files.copy(sourcePhotoPath, Paths.get(this.member.getPhoto()));
		}
		return photoMemberActualPath;
	}

	public String updatePhotoMember(Path sourcePhotoPath) throws IOException {

		String memberPhoto = this.member.getPhoto();

		if (memberPhoto == null || memberPhoto.equalsIgnoreCase("NONE")) { 
			// if photo doesn't exist
			this.member.setPhoto(MEMBER_PHOTO_PATH.resolve(
					this.member.getId() + "").toString());
		} else {
			if (Files.exists(Paths.get(this.member.getPhoto()))) {
				Files.delete(Paths.get(this.member.getPhoto()));
			}
		}
		Files.copy(sourcePhotoPath, Paths.get(this.member.getPhoto()));
		return this.member.getPhoto();
	}


	public InputStream readPhotoMember() throws IOException {
		InputStream photoInputStream = ClassLoader
				.getSystemResourceAsStream(NO_PHOTO);
		if (this.member.getPhoto() != null) {
			if (!this.member.getPhoto().equalsIgnoreCase("NONE")) {
				photoInputStream = Files.newInputStream(Paths.get(this.member
						.getPhoto()));

			}
		}
		return photoInputStream;

	}

}
